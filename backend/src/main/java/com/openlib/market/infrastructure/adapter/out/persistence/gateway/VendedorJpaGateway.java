package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.vendedor.IVendedorGateway;
import com.openlib.market.domain.vendedor.Vendedor;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.VendedorMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.VendedorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

import org.springframework.context.annotation.Primary;

@Component
@Primary
public class VendedorJpaGateway implements IVendedorGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(VendedorJpaGateway.class);

    private final VendedorRepository repository;
    private final VendedorMapper mapper;

    public VendedorJpaGateway(VendedorRepository repository, VendedorMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void guardar(Vendedor vendedor) {
        try {
            LOGGER.info("[PERSISTENCIA] Guardando vendedor en tabla vendedores. id={}, idUsuario={}, nit={}",
                    vendedor.getId(), vendedor.getIdUsuario(), vendedor.getIdentificacionTributaria().getValor());
            repository.save(mapper.toEntity(vendedor));
            repository.flush();
            LOGGER.info("[PERSISTENCIA] Vendedor persistido correctamente en tabla vendedores. id={}", vendedor.getId());
        } catch (DataIntegrityViolationException e) {
            LOGGER.warn("[PERSISTENCIA] Conflicto al guardar vendedor en tabla vendedores. nit={}",
                    vendedor.getIdentificacionTributaria().getValor());
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
