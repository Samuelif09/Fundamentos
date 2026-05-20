package com.openlib.market.infrastructure.suscripcion;

import com.openlib.market.domain.registro.IUsuarioGateway;
import com.openlib.market.domain.registro.RolUsuario;
import com.openlib.market.domain.registro.Usuario;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioDummyGateway implements IUsuarioGateway {

    @Override
    public Optional<Usuario> buscarPorId(String id) {
        // Mock data para probar la historia C-22
        if ("vendedor-1".equals(id)) {
            return Optional.of(new Usuario("vendedor-1", "Vendedor Uno", new com.openlib.market.domain.registro.Email("vend@test.com"), new com.openlib.market.domain.registro.Password("Pass123!"), RolUsuario.VENDEDOR));
        } else if ("user-2".equals(id)) {
            return Optional.of(new Usuario("user-2", "Comprador Dos", new com.openlib.market.domain.registro.Email("comp@test.com"), new com.openlib.market.domain.registro.Password("Pass123!"), RolUsuario.COMPRADOR));
        }
        return Optional.empty();
    }

    @Override
    public void actualizar(Usuario usuario) {
        // Dummy implementation
    }
}
