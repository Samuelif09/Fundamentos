package com.openlib.market.infrastructure.detalle;

import com.openlib.market.domain.detalle.IDetalleGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.Precio;
import com.openlib.market.domain.detalle.EstadoLibro;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.LibroRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
public class DetalleJpaGateway implements IDetalleGateway {

    private final LibroRepository repository;

    public DetalleJpaGateway(LibroRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Libro> buscarPorId(Isbn isbn) {
        return repository.findById(isbn.getValor())
                .map(entity -> new Libro(
                        new Isbn(entity.getIsbn()),
                        entity.getTitulo(),
                        entity.getSinopsis(),
                        new Precio(entity.getPrecio()),
                        entity.getUrlPortada(),
                        entity.getCategoria(),
                        entity.getIdVendedor(),
                        entity.getEstado() != null ? EstadoLibro.valueOf(entity.getEstado()) : EstadoLibro.ACTIVO,
                        entity.getUrlVistaPrevia()
                ));
    }
}
