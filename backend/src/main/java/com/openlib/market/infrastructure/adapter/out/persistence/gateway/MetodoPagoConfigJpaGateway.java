package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.configuracion.ConfiguracionMetodoPago;
import com.openlib.market.domain.configuracion.EstadoMetodoPago;
import com.openlib.market.domain.configuracion.IMetodoPagoConfigGateway;
import com.openlib.market.domain.configuracion.NombreMetodo;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.MetodoPagoConfigEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.MetodoPagoConfigRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Primary
public class MetodoPagoConfigJpaGateway implements IMetodoPagoConfigGateway {

    private final MetodoPagoConfigRepository repository;

    public MetodoPagoConfigJpaGateway(MetodoPagoConfigRepository repository) {
        this.repository = repository;
    }

    @Override
    public void actualizar(ConfiguracionMetodoPago configuracion) {
        MetodoPagoConfigEntity entity = repository.findById(configuracion.getId())
                .orElse(new MetodoPagoConfigEntity(
                        configuracion.getId(),
                        configuracion.getNombre().getValor(),
                        configuracion.getEstado().name()));
        entity.setEstado(configuracion.getEstado().name());
        repository.save(entity);
    }

    @Override
    public Optional<ConfiguracionMetodoPago> obtenerPorId(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<ConfiguracionMetodoPago> listarTodos() {
        return repository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public int contarMetodosHabilitados() {
        return (int) repository.countByEstado(EstadoMetodoPago.HABILITADO.name());
    }

    private ConfiguracionMetodoPago toDomain(MetodoPagoConfigEntity e) {
        return new ConfiguracionMetodoPago(e.getId(), new NombreMetodo(e.getNombre()),
                EstadoMetodoPago.valueOf(e.getEstado()));
    }
}
