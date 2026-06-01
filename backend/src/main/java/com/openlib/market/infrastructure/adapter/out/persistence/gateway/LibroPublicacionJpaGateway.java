package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.curaduria.ICuraduriaGateway;
import com.openlib.market.domain.detalle.ILibroPublicacionGateway;
import com.openlib.market.domain.detalle.EstadoLibro;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.LibroEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.LibroMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ContenidoDigitalRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Primary
public class LibroPublicacionJpaGateway implements ILibroPublicacionGateway, ICuraduriaGateway {

    private final ContenidoDigitalRepository repository;
    private final LibroMapper mapper;

    public LibroPublicacionJpaGateway(ContenidoDigitalRepository repository, LibroMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void guardar(Libro libro) {
        LibroEntity entity = mapper.toEntity(libro);
        repository.save(entity);
        repository.flush();
    }

    @Override
    public void actualizar(Libro libro) {
        repository.save(mapper.toEntity(libro));
        repository.flush();
    }

    @Override
    public Optional<Libro> obtenerPorIsbn(String isbn) {
        return repository.findById(isbn)
                .filter(entity -> entity instanceof LibroEntity)
                .map(entity -> mapper.toDomain((LibroEntity) entity));
    }

    @Override
    public List<Libro> listarPorEstado(EstadoLibro estado, int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        return repository.findByEstado(estado.name(), pageable).stream()
                .filter(entity -> entity instanceof LibroEntity)
                .map(entity -> mapper.toDomain((LibroEntity) entity))
                .toList();
    }
}
