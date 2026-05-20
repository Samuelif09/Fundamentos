package com.openlib.market.infrastructure.notificacion;

import com.openlib.market.application.notificacion.RecibirPostCompraInteractor;
import com.openlib.market.domain.pago.PedidoCompletadoEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SpringDomainEventListenerAdapter {

    private final RecibirPostCompraInteractor interactor;

    // Usamos inyección por constructor. El bean de RecibirPostCompraInteractor se 
    // debe configurar en un AppConfig de Spring.
    public SpringDomainEventListenerAdapter(RecibirPostCompraInteractor interactor) {
        this.interactor = interactor;
    }

    @EventListener
    public void handlePedidoCompletadoEvent(PedidoCompletadoEvent event) {
        // Enlaza el evento de infraestructura de Spring hacia el caso de uso puro de la aplicación
        interactor.onPedidoCompletado(event);
    }
}
