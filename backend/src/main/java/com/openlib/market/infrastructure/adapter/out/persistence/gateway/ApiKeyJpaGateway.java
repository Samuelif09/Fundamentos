package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.api.*;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.CredencialApiEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.CredencialApiRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
public class ApiKeyJpaGateway implements IApiKeyGateway {

    private final CredencialApiRepository repository;

    public ApiKeyJpaGateway(CredencialApiRepository repository) {
        this.repository = repository;
    }

    @Override
    public void guardar(CredencialApi credencial) {
        CredencialApiEntity entity = repository.findById(credencial.getId())
                .orElse(new CredencialApiEntity(credencial.getId(), credencial.getIdPropietario(),
                        credencial.getNombreApp(), credencial.getLlave().valor(),
                        credencial.getEstado().name()));
        entity.setEstado(credencial.getEstado().name());
        repository.save(entity);
    }

    @Override
    public Optional<CredencialApi> buscarPorLlave(String valorLlave) {
        return repository.findByValorLlave(valorLlave).map(this::toDomain);
    }

    @Override
    public Optional<CredencialApi> buscarPorId(String id) {
        return repository.findById(id).map(this::toDomain);
    }

    private CredencialApi toDomain(CredencialApiEntity e) {
        return new CredencialApi(e.getId(), e.getIdPropietario(), e.getNombreApp(),
                e.getValorLlave(), EstadoLlave.valueOf(e.getEstado()));
    }
}
