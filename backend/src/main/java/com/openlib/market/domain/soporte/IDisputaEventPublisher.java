package com.openlib.market.domain.soporte;

public interface IDisputaEventPublisher {
    void publicar(ReembolsoSolicitadoPorDisputaEvent evento);
}
