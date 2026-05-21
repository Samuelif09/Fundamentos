package com.openlib.market.application.notificacion;

import com.openlib.market.domain.notificacion.EmailDestino;
import com.openlib.market.domain.notificacion.INotificacionGateway;
import com.openlib.market.domain.notificacion.ReciboCompra;
import com.openlib.market.domain.pago.PedidoCompletadoEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class RecibirPostCompraInteractorTest {

    private INotificacionGateway notificacionGateway;
    private RecibirPostCompraInteractor interactor;

    @BeforeEach
    void setUp() {
        notificacionGateway = mock(INotificacionGateway.class);
        interactor = new RecibirPostCompraInteractor(notificacionGateway);
    }

    @Test
    void debeInvocarNotificacionGatewayAlRecibirEvento() {
        PedidoCompletadoEvent event = new PedidoCompletadoEvent("order-123", "test@test.com", 250.0, java.util.List.of());
        
        interactor.onPedidoCompletado(event);
        
        verify(notificacionGateway, times(1)).enviarReciboEmail(any(EmailDestino.class), any(ReciboCompra.class));
    }
}
