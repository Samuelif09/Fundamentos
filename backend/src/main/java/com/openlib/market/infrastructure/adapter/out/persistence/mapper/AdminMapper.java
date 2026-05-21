package com.openlib.market.infrastructure.adapter.out.persistence.mapper;

import com.openlib.market.domain.autenticacion.Administrador;
import com.openlib.market.domain.autenticacion.Email;
import com.openlib.market.domain.autenticacion.NombreRolAdmin;
import com.openlib.market.domain.autenticacion.Rol;
import com.openlib.market.domain.autenticacion.RolAdmin;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.AdminEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.AdminRolEmbeddable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdminMapper {

    public Administrador toDomain(AdminEntity entity) {
        List<RolAdmin> roles = entity.getRolesAdmin().stream()
                .map(embeddable -> new RolAdmin(
                        NombreRolAdmin.valueOf(embeddable.getNombreRol()),
                        embeddable.getPermisosComaSeparados() != null && !embeddable.getPermisosComaSeparados().isEmpty()
                                ? Arrays.asList(embeddable.getPermisosComaSeparados().split(","))
                                : List.of()
                ))
                .collect(Collectors.toList());

        return new Administrador(
                entity.getId(),
                new Email(entity.getEmail()),
                entity.getHashContrasena(),
                Rol.valueOf(entity.getRol()),
                roles
        );
    }

    public AdminEntity toEntity(Administrador domain) {
        List<AdminRolEmbeddable> embeddableRoles = domain.getRoles().stream()
                .map(rol -> new AdminRolEmbeddable(
                        rol.getNombre().name(),
                        String.join(",", rol.getPermisos())
                ))
                .collect(Collectors.toList());

        return new AdminEntity(
                domain.getId(),
                domain.getEmail().getDireccion(),
                domain.getHashContrasena(),
                domain.getRol().name(),
                embeddableRoles
        );
    }
}
