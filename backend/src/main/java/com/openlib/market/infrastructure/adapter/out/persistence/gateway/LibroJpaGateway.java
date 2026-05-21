package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.carrito.ILibroGateway;
import com.openlib.market.domain.carrito.LibroSnapshot;
import com.openlib.market.domain.detalle.IActualizarLibroGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.LibroEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.LibroMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ContenidoDigitalRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
public class LibroJpaGateway implements ILibroGateway, IActualizarLibroGateway {

    private final ContenidoDigitalRepository repository;
    private final LibroMapper mapper;

    public LibroJpaGateway(ContenidoDigitalRepository repository, LibroMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // --- ILibroGateway (Carrito) ---
    @Override
    public Optional<LibroSnapshot> obtenerPorIsbn(String isbn) {
        return repository.findById(isbn).map(entity -> new LibroSnapshot(
                entity.getIsbn(),
                entity.getPrecio()
        ));
    }

    // --- IActualizarLibroGateway (Detalle) ---
    @Override
    public Optional<Libro> buscarPorIsbn(Isbn isbn) {
        return repository.findById(isbn.getValor())
                .filter(entity -> entity instanceof LibroEntity)
                .map(entity -> mapper.toDomain((LibroEntity) entity));
    }

    @Override
    public void actualizar(Libro libro) {
        repository.save(mapper.toEntity(libro));
        repository.flush();
    }
}
