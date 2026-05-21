package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.categoria.CategoriaCatalogo;
import com.openlib.market.domain.categoria.ICategoriaGateway;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.CategoriaMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.CategoriaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Primary
public class CategoriaJpaGateway implements ICategoriaGateway {

    private final CategoriaRepository repository;
    private final CategoriaMapper mapper;

    public CategoriaJpaGateway(CategoriaRepository repository, CategoriaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void guardar(CategoriaCatalogo categoria) {
        repository.save(mapper.toEntity(categoria));
        repository.flush();
    }

    @Override
    public void actualizar(CategoriaCatalogo categoria) {
        guardar(categoria);
    }

    @Override
    public Optional<CategoriaCatalogo> obtenerPorId(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean existePorNombreNormalizado(String nombreNormalizado) {
        return repository.findByNombreIgnoreCase(nombreNormalizado).isPresent();
    }

    @Override
    public List<CategoriaCatalogo> listarTodas() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
