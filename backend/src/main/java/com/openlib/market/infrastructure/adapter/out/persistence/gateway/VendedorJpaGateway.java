package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.vendedor.IVendedorGateway;
import com.openlib.market.domain.vendedor.Vendedor;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.VendedorMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.VendedorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

import org.springframework.context.annotation.Primary;

@Component
@Primary
public class VendedorJpaGateway implements IVendedorGateway {

    private final VendedorRepository repository;
    private final VendedorMapper mapper;

    public VendedorJpaGateway(VendedorRepository repository, VendedorMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void guardar(Vendedor vendedor) {
        try {
            repository.save(mapper.toEntity(vendedor));
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("La identificación tributaria ya se encuentra registrada");
        }
    }

    @Override
    public void actualizar(Vendedor vendedor) {
        repository.save(mapper.toEntity(vendedor));
    }

    @Override
    public boolean existePorIdentificacionTributaria(String identificacionTributaria) {
        return repository.existsByIdentificacionTributaria(identificacionTributaria);
    }

    @Override
    public Optional<Vendedor> obtenerPorId(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }
}
