package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.finanzas.FacturaTributaria;
import com.openlib.market.domain.finanzas.IFacturacionGateway;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.FacturacionMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.FacturaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
public class FacturacionJpaGateway implements IFacturacionGateway {

    private final FacturaRepository repository;
    private final FacturacionMapper mapper;

    public FacturacionJpaGateway(FacturaRepository repository, FacturacionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void guardarFactura(FacturaTributaria factura) {
        repository.save(mapper.toEntity(factura));
        repository.flush();
    }

    @Override
    public Optional<FacturaTributaria> obtenerPorId(String idFactura) {
        return repository.findById(idFactura).map(mapper::toDomain);
    }
}
