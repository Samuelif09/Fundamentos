package com.openlib.market.infrastructure.catalogo;

import com.openlib.market.domain.pago.PedidoCompletadoEvent;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ContenidoDigitalRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ReducirStockVentaEventListener {

    private final ContenidoDigitalRepository contenidoDigitalRepository;

    public ReducirStockVentaEventListener(ContenidoDigitalRepository contenidoDigitalRepository) {
        this.contenidoDigitalRepository = contenidoDigitalRepository;
    }

    @Async
    @EventListener
    @Transactional
    public void handlePedidoCompletado(PedidoCompletadoEvent event) {
        if (event.getIsbns() != null) {
            for (String isbn : event.getIsbns()) {
                contenidoDigitalRepository.decrementStock(isbn);
            }
        }
    }
}
