package com.openlib.market.application.ventavendedor;

import com.openlib.market.domain.pago.PedidoCompletadoEvent;
import com.openlib.market.domain.ventavendedor.IDetalleLibroGateway;
import com.openlib.market.domain.ventavendedor.INotificacionVendedorGateway;
import com.openlib.market.domain.ventavendedor.NotificacionVendedor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class RecibirVentasInteractorTest {

    private IDetalleLibroGateway detalleLibroGateway;
    private INotificacionVendedorGateway notificacionGateway;
    private RecibirVentasInteractor interactor;

    @BeforeEach
    void setUp() {
        detalleLibroGateway = mock(IDetalleLibroGateway.class);
        notificacionGateway = mock(INotificacionVendedorGateway.class);
        interactor = new RecibirVentasInteractor(detalleLibroGateway, notificacionGateway);
    }

    @Test
    void debeNotificarAcadaVendedorSusPropiosLibros() {
        when(detalleLibroGateway.obtenerIdVendedorPorIsbn("isbn-1")).thenReturn(Optional.of("seller-A"));
        when(detalleLibroGateway.obtenerIdVendedorPorIsbn("isbn-2")).thenReturn(Optional.of("seller-B"));
        when(detalleLibroGateway.obtenerIdVendedorPorIsbn("isbn-3")).thenReturn(Optional.of("seller-A"));

        PedidoCompletadoEvent event = new PedidoCompletadoEvent(
                "order-1", "user-1", 100.0, List.of("isbn-1", "isbn-2", "isbn-3")
        );

        interactor.onPedidoCompletado(event);

        verify(notificacionGateway, times(2)).notificarVenta(any(NotificacionVendedor.class));
        verify(notificacionGateway).notificarVenta(argThat(n -> n.getIdVendedor().equals("seller-A") && n.getIsbnsVendidos().size() == 2));
        verify(notificacionGateway).notificarVenta(argThat(n -> n.getIdVendedor().equals("seller-B") && n.getIsbnsVendidos().size() == 1));
    }

    @Test
    void noHaceNadaSiNoHayIsbns() {
        PedidoCompletadoEvent event = new PedidoCompletadoEvent("order-1", "user-1", 100.0, List.of());
        interactor.onPedidoCompletado(event);
        verify(notificacionGateway, never()).notificarVenta(any());
    }
}
