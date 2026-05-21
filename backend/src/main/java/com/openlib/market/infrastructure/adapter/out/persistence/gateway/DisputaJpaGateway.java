package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.soporte.*;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.DisputaEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.DisputaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
public class DisputaJpaGateway implements IDisputaGateway {

    private final DisputaRepository repository;

    public DisputaJpaGateway(DisputaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Disputa> buscarPorId(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public void guardar(Disputa disputa) {
        DisputaEntity entity = repository.findById(disputa.getId())
                .orElse(new DisputaEntity(disputa.getId(), disputa.getIdPedido(),
                        disputa.getIdComprador(), disputa.getIdVendedor(),
                        disputa.getMotivo(), disputa.getEstado().name(),
                        disputa.getResolucion().name()));
        entity.setEstado(disputa.getEstado().name());
        entity.setResolucion(disputa.getResolucion().name());
        repository.save(entity);
    }

    private Disputa toDomain(DisputaEntity e) {
        return new Disputa(e.getId(), e.getIdPedido(), e.getIdComprador(), e.getIdVendedor(),
                e.getMotivo(), EstadoDisputa.valueOf(e.getEstado()),
                Resolucion.valueOf(e.getResolucion()));
    }
}
