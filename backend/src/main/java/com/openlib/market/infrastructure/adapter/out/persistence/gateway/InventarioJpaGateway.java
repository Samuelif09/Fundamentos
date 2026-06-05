package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.inventario.IInventarioGateway;
import com.openlib.market.domain.inventario.StockDisponible;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ContenidoDigitalRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

import java.util.stream.Collectors;
import java.util.List;

@Component
@Primary
public class InventarioJpaGateway implements com.openlib.market.domain.inventario.IInventarioGateway, com.openlib.market.domain.catalogo.IInventarioGateway {

    private final ContenidoDigitalRepository repository;
    private final com.openlib.market.infrastructure.adapter.out.persistence.repository.VendedorRepository vendedorRepository;

    public InventarioJpaGateway(ContenidoDigitalRepository repository, com.openlib.market.infrastructure.adapter.out.persistence.repository.VendedorRepository vendedorRepository) {
        this.repository = repository;
        this.vendedorRepository = vendedorRepository;
    }

    @Override
    public Optional<StockDisponible> obtenerStock(String isbn) {
        return repository.findById(isbn)
                .map(entity -> new StockDisponible(entity.getStockDisponible()));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void agregarStock(String productoId, int cantidad) {
        var entity = repository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("El producto con ID " + productoId + " no existe."));
        entity.setStockDisponible(entity.getStockDisponible() + cantidad);
        repository.save(entity);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void restarStock(String productoId, int cantidad) {
        var entity = repository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("El producto con ID " + productoId + " no existe."));
        entity.setStockDisponible(entity.getStockDisponible() - cantidad);
        repository.save(entity);
    }

    @Override
    public List<com.openlib.market.domain.catalogo.LibroCatalogo> listarPorVendedorId(String idVendedorUsuario) {
        String realIdVendedor = vendedorRepository.findByIdUsuario(idVendedorUsuario)
                .map(v -> v.getId())
                .orElse(idVendedorUsuario);

        return repository.findAll().stream()
                .filter(entity -> realIdVendedor.equals(entity.getIdVendedor()))
                .map(entity -> new com.openlib.market.domain.catalogo.LibroCatalogo(
                        entity.getIsbn(),
                        entity.getTitulo(),
                        entity.getPrecio(),
                        entity.getUrlPortada()
                ))
                .collect(Collectors.toList());
    }
}
