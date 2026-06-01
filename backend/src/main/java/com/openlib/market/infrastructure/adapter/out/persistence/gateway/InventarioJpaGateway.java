package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.inventario.IInventarioGateway;
import com.openlib.market.domain.inventario.StockDisponible;
import com.openlib.market.domain.catalogo.LibroCatalogo;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ContenidoDigitalRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Primary
public class InventarioJpaGateway implements IInventarioGateway, com.openlib.market.domain.catalogo.IInventarioGateway {

    private final ContenidoDigitalRepository repository;

    public InventarioJpaGateway(ContenidoDigitalRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<StockDisponible> obtenerStock(String isbn) {
        return repository.findById(isbn)
                .map(entity -> new StockDisponible(entity.getStockDisponible()));
    }

    @Override
    public List<LibroCatalogo> listarPorVendedorId(String idVendedor) {
        return repository.findByIdVendedor(idVendedor).stream()
                .map(entity -> new LibroCatalogo(
                        entity.getIsbn(),
                        entity.getTitulo(),
                        entity.getPrecio(),
                        entity.getUrlPortada(),
                        entity.getStockDisponible(),
                        entity.getEstado() != null ? entity.getEstado() : "ACTIVO"
                ))
                .collect(Collectors.toList());
    }
}
