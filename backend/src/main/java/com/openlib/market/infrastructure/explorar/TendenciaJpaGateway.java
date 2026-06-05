package com.openlib.market.infrastructure.explorar;

import com.openlib.market.domain.explorar.ITendenciaGateway;
import com.openlib.market.domain.explorar.LibroTendencia;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.LibroRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Primary
public class TendenciaJpaGateway implements ITendenciaGateway {

    private final LibroRepository repository;

    public TendenciaJpaGateway(LibroRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<LibroTendencia> obtenerTodos() {
        return repository.findAll().stream()
                .map(entity -> new LibroTendencia(
                        entity.getIsbn(),
                        entity.getTitulo(),
                        entity.getVentasTotales(),
                        entity.getPromedioCalificacion(),
                        entity.getFechaPublicacion()
                ))
                .collect(Collectors.toList());
    }
}
