package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.autenticacion.Administrador;
import com.openlib.market.domain.autenticacion.Email;
import com.openlib.market.domain.autenticacion.IAdminGateway;
import com.openlib.market.domain.autenticacion.NombreRolAdmin;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.AdminMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.AdminRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Primary
public class AdminJpaGateway implements IAdminGateway {

    private final AdminRepository repository;
    private final AdminMapper mapper;

    public AdminJpaGateway(AdminRepository repository, AdminMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Administrador> buscarPorEmail(Email email) {
        return repository.findByEmail(email.getDireccion()).map(mapper::toDomain);
    }

    @Override
    public Optional<Administrador> buscarPorId(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void guardar(Administrador administrador) {
        repository.save(mapper.toEntity(administrador));
        repository.flush();
    }

    @Override
    public int contarSuperAdmins() {
        return (int) repository.findAll().stream()
                .filter(admin -> admin.getRolesAdmin().stream()
                        .anyMatch(rol -> NombreRolAdmin.SUPERADMIN.name().equals(rol.getNombreRol())))
                .count();
    }
}
