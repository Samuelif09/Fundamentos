package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.inventario.IInventarioGateway;
import com.openlib.market.domain.inventario.StockDisponible;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ContenidoDigitalRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
public class InventarioJpaGateway implements IInventarioGateway {

    private final ContenidoDigitalRepository repository;

    public InventarioJpaGateway(ContenidoDigitalRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<StockDisponible> obtenerStock(String isbn) {
        return repository.findById(isbn)
                .map(entity -> new StockDisponible(entity.getStockDisponible()));
    }
}
