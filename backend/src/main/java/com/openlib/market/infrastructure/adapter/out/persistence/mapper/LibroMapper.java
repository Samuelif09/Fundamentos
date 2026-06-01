package com.openlib.market.infrastructure.adapter.out.persistence.mapper;

import com.openlib.market.domain.catalogo.LibroCatalogo;
import com.openlib.market.domain.detalle.EstadoLibro;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.Precio;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.LibroEntity;
import org.springframework.stereotype.Component;

@Component
public class LibroMapper {

    public Libro toDomain(LibroEntity entity) {
        return new Libro(
                new Isbn(entity.getIsbn()),
                entity.getTitulo(),
                entity.getSinopsis(),
                new Precio(entity.getPrecio()),
                entity.getUrlPortada(),
                entity.getCategoria(),
                entity.getIdVendedor(),
                EstadoLibro.valueOf(entity.getEstado() != null ? entity.getEstado() : "ACTIVO"),
                entity.getUrlVistaPrevia(),
                entity.getStockDisponible()
        );
    }

    public LibroEntity toEntity(Libro domain) {
        LibroEntity entity = new LibroEntity();
        entity.setIsbn(domain.getIsbn().getValor());
        entity.setTitulo(domain.getTitulo());
        entity.setSinopsis(domain.getSinopsis());
        entity.setPrecio(domain.getPrecio().getValor());
        entity.setUrlPortada(domain.getUrlPortada());
        entity.setCategoria(domain.getCategoria());
        entity.setIdVendedor(domain.getIdVendedor());
        entity.setEstado(domain.getEstado().name());
        entity.setUrlVistaPrevia(domain.getUrlVistaPrevia());
        entity.setStockDisponible(domain.getStock());
        return entity;
    }

    public LibroCatalogo toCatalogoDomain(LibroEntity entity) {
        return new LibroCatalogo(
                entity.getIsbn(),
                entity.getTitulo(),
                entity.getPrecio(),
                entity.getUrlPortada()
        );
    }
}
