package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.autenticacion.IUsuarioAuthGateway;
import com.openlib.market.domain.autenticacion.UsuarioAuth;
import com.openlib.market.domain.registro.Email;
import com.openlib.market.domain.registro.IRegistroGateway;
import com.openlib.market.domain.registro.IUsuarioGateway;
import com.openlib.market.domain.registro.Usuario;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.UsuarioMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;

@Component
@Primary
public class UsuarioJpaGateway implements IUsuarioGateway, IRegistroGateway, IUsuarioAuthGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioJpaGateway.class);

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;

    public UsuarioJpaGateway(UsuarioRepository repository, UsuarioMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // --- IUsuarioGateway ---

    @Override
    public Optional<Usuario> buscarPorId(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void actualizar(Usuario usuario) {
        repository.save(mapper.toEntity(usuario));
    }

    @Override
    public List<Usuario> listarTodos() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    // --- IRegistroGateway ---

    @Override
    public void guardar(Usuario usuario) {
        try {
            LOGGER.info("[PERSISTENCIA] Guardando usuario en tabla usuarios. id={}, email={}", usuario.getId(), usuario.getEmail().getValor());
            repository.save(mapper.toEntity(usuario));
            repository.flush();
            LOGGER.info("[PERSISTENCIA] Usuario persistido correctamente en tabla usuarios. id={}", usuario.getId());
        } catch (DataIntegrityViolationException e) {
            LOGGER.warn("[PERSISTENCIA] Conflicto al guardar usuario en tabla usuarios. email={}", usuario.getEmail().getValor());
            // Asumimos que la unica restriccion unica es el email
            throw new IllegalArgumentException("El email ya se encuentra registrado");
        }
    }

    @Override
    public boolean existeEmail(Email email) {
        return repository.existsByEmail(email.getValor());
    }

    // --- IUsuarioAuthGateway ---

    @Override
    public Optional<UsuarioAuth> buscarPorEmail(com.openlib.market.domain.autenticacion.Email email) {
        return repository.findByEmail(email.getDireccion()).map(mapper::toAuthDomain);
    }
}
