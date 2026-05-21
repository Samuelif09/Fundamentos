package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.finanzas.BilleteraVendedor;
import com.openlib.market.domain.finanzas.IBilleteraGateway;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.BilleteraEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.BilleteraRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
public class BilleteraJpaGateway implements IBilleteraGateway {

    private final BilleteraRepository repository;

    public BilleteraJpaGateway(BilleteraRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<BilleteraVendedor> obtenerPorIdVendedor(String idVendedor) {
        return repository.findById(idVendedor)
                .map(e -> new BilleteraVendedor(e.getIdVendedor(), e.getSaldoDisponible()));
    }

    @Override
    public void guardar(BilleteraVendedor billetera) {
        BilleteraEntity entity = repository.findById(billetera.getIdVendedor())
                .orElse(new BilleteraEntity(billetera.getIdVendedor(), billetera.getSaldoDisponible()));
        entity.setSaldoDisponible(billetera.getSaldoDisponible());
        repository.saveAndFlush(entity);
    }
}
