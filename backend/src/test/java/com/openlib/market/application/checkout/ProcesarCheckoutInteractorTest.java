package com.openlib.market.application.checkout;

import com.openlib.market.domain.carrito.Cantidad;
import com.openlib.market.domain.carrito.CarritoCompras;
import com.openlib.market.domain.carrito.ICarritoGateway;
import com.openlib.market.domain.carrito.LibroSnapshot;
import com.openlib.market.domain.carrito.SesionId;
import com.openlib.market.domain.checkout.*;
import com.openlib.market.domain.pago.EstadoPedido;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.pago.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcesarCheckoutInteractorTest {

    private ICarritoGateway carritoGateway;
    private IPedidoGateway pedidoGateway;
    private IPasarelaPagoSimuladaGateway pasarelaPagoGateway;
    private ICheckoutEventPublisher eventPublisher;
    private PedidoFactory pedidoFactory;
    private com.openlib.market.domain.inventario.IInventarioGateway inventarioGateway;
    private com.openlib.market.domain.detalle.IContenidoDigitalGateway contenidoGateway;
    private ProcesarCheckoutInteractor interactor;

    @BeforeEach
    void setUp() {
        carritoGateway = mock(ICarritoGateway.class);
        pedidoGateway = mock(IPedidoGateway.class);
        pasarelaPagoGateway = mock(IPasarelaPagoSimuladaGateway.class);
        eventPublisher = mock(ICheckoutEventPublisher.class);
        pedidoFactory = new PedidoFactory();
        inventarioGateway = mock(com.openlib.market.domain.inventario.IInventarioGateway.class);
        contenidoGateway = mock(com.openlib.market.domain.detalle.IContenidoDigitalGateway.class);

        interactor = new ProcesarCheckoutInteractor(
                carritoGateway,
                pedidoGateway,
                pasarelaPagoGateway,
                eventPublisher,
                pedidoFactory,
                inventarioGateway,
                contenidoGateway
        );
    }

    @Test
    void debeProcesarCheckoutExitosamente() {
        String sesionIdStr = "sesion-123";
        SesionId sesionId = new SesionId(sesionIdStr);
        CarritoCompras carrito = new CarritoCompras(sesionId);
        carrito.agregarItem(new LibroSnapshot("123", 100.0), new Cantidad(1));
        
        when(carritoGateway.obtenerPorSesionId(any(SesionId.class))).thenReturn(Optional.of(carrito));
        when(pasarelaPagoGateway.procesarPago(anyDouble())).thenReturn("txn-123");

        interactor.ejecutar(sesionIdStr, "user-123", "TARJETA");

        ArgumentCaptor<Pedido> pedidoCaptor = ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoGateway).guardar(pedidoCaptor.capture());

        Pedido pedidoGuardado = pedidoCaptor.getValue();
        assertEquals(EstadoPedido.PAGADO, pedidoGuardado.getEstado());

        ArgumentCaptor<CheckoutCompletadoEvent> eventCaptor = ArgumentCaptor.forClass(CheckoutCompletadoEvent.class);
        verify(eventPublisher).publicar(eventCaptor.capture());
        
        CheckoutCompletadoEvent evento = eventCaptor.getValue();
        assertEquals(pedidoGuardado.getId(), evento.getPedidoId());
        assertEquals(sesionIdStr, evento.getSesionId());
    }

    @Test
    void debeCalcularTotalConDecoradores() {
        // Validación del Decorator
        CalculadorPrecio calculadorBase = new PrecioBase(100.0);
        CalculadorPrecio conImpuesto = new ImpuestoDecorator(calculadorBase, 0.10); // 10%

        assertEquals(110.0, conImpuesto.calcularTotal());
    }
}
