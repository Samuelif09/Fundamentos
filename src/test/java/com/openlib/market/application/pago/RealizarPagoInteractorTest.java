package com.openlib.market.application.pago;

import com.openlib.market.domain.carrito.CarritoCompras;
import com.openlib.market.domain.carrito.ICarritoGateway;
import com.openlib.market.domain.carrito.SesionId;
import com.openlib.market.domain.pago.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RealizarPagoInteractorTest {

    private IPedidoGateway pedidoGateway;
    private IPasarelaPagoFactory pasarelaFactory;
    private IPagoExternoGateway pagoExternoGateway;
    private ICarritoGateway carritoGateway;
    private RealizarPagoInteractor interactor;

    @BeforeEach
    void setUp() {
        pedidoGateway = mock(IPedidoGateway.class);
        pasarelaFactory = mock(IPasarelaPagoFactory.class);
        pagoExternoGateway = mock(IPagoExternoGateway.class);
        carritoGateway = mock(ICarritoGateway.class);
        interactor = new RealizarPagoInteractor(pedidoGateway, pasarelaFactory, carritoGateway);
    }

    @Test
    void debeProcesarPagoExitosoCambiarEstadoYVaciarCarrito() {
        RealizarPagoRequestDto request = new RealizarPagoRequestDto("sesion1", 100.0, "TARJETA", "1234");
        when(pasarelaFactory.obtenerPasarela(TipoMetodoPago.TARJETA)).thenReturn(pagoExternoGateway);
        when(pagoExternoGateway.procesar(eq(100.0), any(MetodoPago.class))).thenReturn(true);
        when(carritoGateway.obtenerPorSesionId(any(SesionId.class)))
            .thenReturn(Optional.of(new CarritoCompras(new SesionId("sesion1"))));

        interactor.realizarPago(request);

        verify(pedidoGateway, times(2)).guardar(any(Pedido.class)); // 1. Pendiente 2. Pagado
        verify(carritoGateway).guardar(any(CarritoCompras.class)); // Vacía carrito
    }

    @Test
    void debeFallarPedidoYNoVaciarCarritoSiPagoRechazado() {
        RealizarPagoRequestDto request = new RealizarPagoRequestDto("sesion1", 100.0, "TARJETA", "1234");
        when(pasarelaFactory.obtenerPasarela(TipoMetodoPago.TARJETA)).thenReturn(pagoExternoGateway);
        when(pagoExternoGateway.procesar(eq(100.0), any(MetodoPago.class))).thenReturn(false); // Rechazado

        assertThrows(PagoRechazadoException.class, () -> interactor.realizarPago(request));

        verify(pedidoGateway, times(2)).guardar(any(Pedido.class)); // 1. Pendiente 2. Fallido
        verify(carritoGateway, never()).guardar(any(CarritoCompras.class)); // No vacía carrito
    }
}
