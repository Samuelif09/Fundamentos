package com.openlib.market.application.soporte;

import com.openlib.market.domain.soporte.*;

public class GestionarSoporteInteractor implements IGestionarSoporteUseCase {

    private final IDisputaGateway disputaGateway;
    private final IDisputaEventPublisher eventPublisher;

    public GestionarSoporteInteractor(IDisputaGateway disputaGateway, IDisputaEventPublisher eventPublisher) {
        this.disputaGateway = disputaGateway;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public DisputaDto iniciarMediacion(String disputaId) {
        Disputa disputa = disputaGateway.buscarPorId(disputaId)
                .orElseThrow(() -> new IllegalArgumentException("Disputa no encontrada"));

        disputa.iniciarMediacion();
        disputaGateway.guardar(disputa);

        return new DisputaDto(disputa.getId(), disputa.getIdPedido(), disputa.getEstado().name(), disputa.getResolucion().name());
    }

    @Override
    public DisputaDto resolverDisputa(String disputaId, String resolucionStr) {
        Disputa disputa = disputaGateway.buscarPorId(disputaId)
                .orElseThrow(() -> new IllegalArgumentException("Disputa no encontrada"));

        Resolucion dictamen = Resolucion.valueOf(resolucionStr.toUpperCase());
        
        ReembolsoSolicitadoPorDisputaEvent evento = disputa.resolver(dictamen);
        disputaGateway.guardar(disputa);

        if (evento != null) {
            eventPublisher.publicar(evento);
        }

        return new DisputaDto(disputa.getId(), disputa.getIdPedido(), disputa.getEstado().name(), disputa.getResolucion().name());
    }
}
