package com.openlib.market.application.pago;

import com.openlib.market.domain.pago.*;
import com.openlib.market.domain.carrito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IngresarCheckoutInteractorTest {

    private IPasarelaPagoGateway pasarelaPago;
    private IEventPublisher eventPublisher;
    private ICarritoGateway carritoGateway;
    private IPedidoGateway pedidoGateway;
    private IngresarCheckoutInteractor interactor;

    @BeforeEach
    void setUp() {
        pasarelaPago = mock(IPasarelaPagoGateway.class);
        eventPublisher = mock(IEventPublisher.class);
        carritoGateway = mock(ICarritoGateway.class);
        pedidoGateway = mock(IPedidoGateway.class);
        interactor = new IngresarCheckoutInteractor(pasarelaPago, eventPublisher, carritoGateway, pedidoGateway);
    }

    @Test
    void debeLanzarExcepcionYNoPublicarEventoSiPagoEsRechazado() {
        when(pasarelaPago.procesarCobro(any(TokenPago.class), any(Monto.class))).thenReturn(false);
        CarritoCompras carrito = new CarritoCompras(new IdUsuario("user-1"));
        carrito.agregarItem(new LibroSnapshot("isbn", 100.0), new Cantidad(1));
        when(carritoGateway.obtenerPorUsuario(any(IdUsuario.class))).thenReturn(Optional.of(carrito));

        CheckoutRequestDto req = new CheckoutRequestDto("user-1", "order-1", 100.0, "tok_invalid");

        assertThrows(PagoRechazadoException.class, () -> interactor.procesarCheckout(req));

        verify(eventPublisher, never()).publicar(any());
    }

    @Test
    void debeProcesarExitosamenteYPublicarEvento() {
        when(pasarelaPago.procesarCobro(any(TokenPago.class), any(Monto.class))).thenReturn(true);
        CarritoCompras carrito = new CarritoCompras(new IdUsuario("user-2"));
        carrito.agregarItem(new LibroSnapshot("isbn", 150.0), new Cantidad(1));
        when(carritoGateway.obtenerPorUsuario(any(IdUsuario.class))).thenReturn(Optional.of(carrito));

        CheckoutRequestDto req = new CheckoutRequestDto("user-2", "order-2", 150.0, "tok_valid");

        assertDoesNotThrow(() -> interactor.procesarCheckout(req));

        verify(eventPublisher, times(1)).publicar(any(PedidoCompletadoEvent.class));
        verify(pedidoGateway, times(1)).guardar(any(Pedido.class));
    }
}
