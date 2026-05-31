package com.openlib.market.infrastructure.adapter.out.persistence.mapper;

import com.openlib.market.domain.registro.Email;
import com.openlib.market.domain.registro.EstadoCuenta;
import com.openlib.market.domain.registro.MotivoSuspension;
import com.openlib.market.domain.registro.Password;
import com.openlib.market.domain.registro.RolUsuario;
import com.openlib.market.domain.registro.Usuario;
import com.openlib.market.domain.autenticacion.UsuarioAuth;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioEntity toEntity(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setNombre(usuario.getNombre());
        entity.setEmail(usuario.getEmail().getValor());
        entity.setPassword(usuario.getPassword().getValor());
        entity.setRol(usuario.getRol().name());
        entity.setEstadoCuenta(usuario.getEstadoCuenta().name());
        if (usuario.getMotivoSuspension() != null) {
            entity.setMotivoSuspension(usuario.getMotivoSuspension().getRazon());
        }
        return entity;
    }

    public Usuario toDomain(UsuarioEntity entity) {
        MotivoSuspension motivo = null;
        if (entity.getMotivoSuspension() != null && !entity.getMotivoSuspension().isEmpty()) {
            motivo = new MotivoSuspension(entity.getMotivoSuspension());
        }
        
        return new Usuario(
                entity.getId(),
                entity.getNombre(),
                new Email(entity.getEmail()),
                new Password(entity.getPassword()),
                RolUsuario.valueOf(entity.getRol()),
                EstadoCuenta.valueOf(entity.getEstadoCuenta()),
                motivo
        );
    }

    public UsuarioAuth toAuthDomain(UsuarioEntity entity) {
        return new UsuarioAuth(
                entity.getId(),
                new com.openlib.market.domain.autenticacion.Email(entity.getEmail()),
                entity.getPassword(),
                entity.getRol()
        );
    }
}
