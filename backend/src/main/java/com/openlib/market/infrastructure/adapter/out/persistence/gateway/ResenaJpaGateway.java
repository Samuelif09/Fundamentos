package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.resena.IResenaGateway;
import com.openlib.market.domain.resena.Resena;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ContenidoDigitalEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.ResenaEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.ResenaMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ContenidoDigitalRepository;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.ResenaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Primary
public class ResenaJpaGateway implements IResenaGateway {

    private final ResenaRepository resenaRepository;
    private final ContenidoDigitalRepository contenidoDigitalRepository;
    private final ResenaMapper mapper;

    public ResenaJpaGateway(ResenaRepository resenaRepository, 
                            ContenidoDigitalRepository contenidoDigitalRepository, 
                            ResenaMapper mapper) {
        this.resenaRepository = resenaRepository;
        this.contenidoDigitalRepository = contenidoDigitalRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Resena> buscarResenasPorIsbn(String isbn) {
        // En un caso real con Pageable, aquí solo traemos todas.
        // Pero idealmente el fetch LAZY lo probamos en ContenidoDigitalEntity
        return resenaRepository.findAll().stream()
                .filter(r -> r.getIsbnLibro().equals(isbn))
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Resena> listarPorLibroId(String isbnLibro, int offset, int limit) {
        return resenaRepository.findAll().stream()
                .filter(r -> r.getIsbnLibro().equals(isbnLibro))
                .skip(offset)
                .limit(limit)
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Resena> obtenerPorId(String id) {
        return resenaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void actualizar(Resena resena) {
        ContenidoDigitalEntity libro = contenidoDigitalRepository.findById(resena.getIsbnLibro())
                .orElseThrow(() -> new IllegalArgumentException("Libro no encontrado"));
                
        ResenaEntity entity = mapper.toEntity(resena, libro);
        resenaRepository.saveAndFlush(entity);

        Double promedio = resenaRepository.calcularPromedioPorIsbn(resena.getIsbnLibro());
        libro.setPromedioCalificacion(promedio != null ? promedio : 0.0);
        contenidoDigitalRepository.save(libro);
    }

    @Override
    public void eliminar(String id) {
        resenaRepository.findById(id).ifPresent(entity -> {
            String isbn = entity.getIsbnLibro();
            resenaRepository.delete(entity);
            resenaRepository.flush();

            Double promedio = resenaRepository.calcularPromedioPorIsbn(isbn);
            contenidoDigitalRepository.findById(isbn).ifPresent(libro -> {
                libro.setPromedioCalificacion(promedio != null ? promedio : 0.0);
                contenidoDigitalRepository.save(libro);
            });
        });
    }
}
