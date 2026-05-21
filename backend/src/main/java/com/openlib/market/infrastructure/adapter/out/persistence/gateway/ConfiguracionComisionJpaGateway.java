package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.configuracion.IConfiguracionComisionGateway;
import com.openlib.market.domain.configuracion.ReglaComision;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ReglaComisionEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ReglaComisionRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Primary
public class ConfiguracionComisionJpaGateway implements IConfiguracionComisionGateway {

    private final ReglaComisionRepository repository;

    public ConfiguracionComisionJpaGateway(ReglaComisionRepository repository) {
        this.repository = repository;
    }

    @Override
    public ReglaComision obtenerRegla(String idCategoria) {
        // Buscar específica, si no hay, buscar GLOBAL, si no hay, asumir 0%
        return repository.findByIdCategoria(idCategoria.toUpperCase())
                .or(() -> repository.findByIdCategoria("GLOBAL"))
                .map(this::toDomain)
                .orElse(new ReglaComision("GLOBAL", 0.0));
    }

    @Override
    public void guardarRegla(ReglaComision regla) {
        ReglaComisionEntity entity = repository.findByIdCategoria(regla.getIdCategoria())
                .orElse(new ReglaComisionEntity(regla.getId(), regla.getIdCategoria(), regla.getPorcentajeComision()));
        entity.setPorcentajeComision(regla.getPorcentajeComision());
        repository.save(entity);
    }

    @Override
    public List<ReglaComision> listarTodas() {
        return repository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    private ReglaComision toDomain(ReglaComisionEntity e) {
        return new ReglaComision(e.getId(), e.getIdCategoria(), e.getPorcentajeComision());
    }
}
