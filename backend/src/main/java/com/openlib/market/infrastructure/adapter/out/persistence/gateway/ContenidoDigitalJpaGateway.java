package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.detalle.ContenidoDigital;
import com.openlib.market.domain.detalle.IContenidoDigitalGateway;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.ContenidoDigitalMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ContenidoDigitalRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
public class ContenidoDigitalJpaGateway implements IContenidoDigitalGateway {

    private final ContenidoDigitalRepository repository;
    private final ContenidoDigitalMapper mapper;

    public ContenidoDigitalJpaGateway(ContenidoDigitalRepository repository, ContenidoDigitalMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void guardarContenido(ContenidoDigital contenido) {
        repository.save(mapper.toEntity(contenido));
        repository.flush();
    }

    @Override
    public Optional<ContenidoDigital> obtenerContenidoPorId(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }
}
