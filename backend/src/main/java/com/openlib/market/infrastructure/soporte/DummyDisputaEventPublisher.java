package com.openlib.market.infrastructure.soporte;

import com.openlib.market.domain.soporte.IDisputaEventPublisher;
import com.openlib.market.domain.soporte.ReembolsoSolicitadoPorDisputaEvent;
import org.springframework.stereotype.Component;

@Component
public class DummyDisputaEventPublisher implements IDisputaEventPublisher {
    @Override
    public void publicar(ReembolsoSolicitadoPorDisputaEvent evento) {
        System.out.println(">>> [EVENTO PUBLICADO] Se ha solicitado un reembolso para el pedido " 
                + evento.idPedido() + " por la disputa " + evento.idDisputa());
        // En una implementación real, esto enviaría el evento a RabbitMQ/Kafka o llamaría a IGestionarReembolsosUseCase
    }
}
