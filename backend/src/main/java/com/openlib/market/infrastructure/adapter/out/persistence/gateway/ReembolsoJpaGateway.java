package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.reembolso.EstadoReembolso;
import com.openlib.market.domain.reembolso.IReembolsoGateway;
import com.openlib.market.domain.reembolso.SolicitudReembolso;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.SolicitudReembolsoEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.SolicitudReembolsoRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Primary
public class ReembolsoJpaGateway implements IReembolsoGateway {

    private final SolicitudReembolsoRepository repository;

    public ReembolsoJpaGateway(SolicitudReembolsoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void guardar(SolicitudReembolso solicitud) {
        repository.save(toEntity(solicitud));
    }

    @Override
    public void actualizar(SolicitudReembolso solicitud) {
        SolicitudReembolsoEntity entity = repository.findById(solicitud.getId())
                .orElseThrow(() -> new IllegalArgumentException("Reembolso no encontrado: " + solicitud.getId()));
        entity.setEstado(solicitud.getEstado().name());
        repository.saveAndFlush(entity);
    }

    @Override
    public Optional<SolicitudReembolso> obtenerPorId(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<SolicitudReembolso> listarTodas() {
        return repository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    private SolicitudReembolsoEntity toEntity(SolicitudReembolso s) {
        return new SolicitudReembolsoEntity(
                s.getId(), s.getIdPedido(), s.getMontoReembolso(), s.getMotivo(), s.getEstado().name());
    }

    private SolicitudReembolso toDomain(SolicitudReembolsoEntity e) {
        return new SolicitudReembolso(
                e.getId(), e.getIdPedido(), e.getMontoReembolso(), e.getMotivo(),
                EstadoReembolso.valueOf(e.getEstado()));
    }
}
