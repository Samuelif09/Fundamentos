package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.registro.Email;
import com.openlib.market.domain.registro.Password;
import com.openlib.market.domain.registro.Usuario;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.UsuarioMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({UsuarioJpaGateway.class, UsuarioMapper.class})
@Transactional
class UsuarioJpaGatewayIT {

    @Autowired
    private UsuarioJpaGateway gateway;

    @Autowired
    private UsuarioRepository repository;

    @Test
    void debeGuardarYRecuperarUsuarioPorId() {
        Usuario nuevoUsuario = new Usuario("Juan Perez", new Email("juan@example.com"), new Password("Password123!"));
        
        gateway.guardar(nuevoUsuario);

        Optional<Usuario> recuperado = gateway.buscarPorId(nuevoUsuario.getId());
        assertTrue(recuperado.isPresent());
        assertEquals("Juan Perez", recuperado.get().getNombre());
        assertEquals("juan@example.com", recuperado.get().getEmail().getValor());
        
        // Verificar que persiste correctamente en DB
        Optional<UsuarioEntity> entity = repository.findById(nuevoUsuario.getId());
        assertTrue(entity.isPresent());
        assertEquals("juan@example.com", entity.get().getEmail());
    }

    @Test
    void debeBuscarUsuarioPorEmailExitosamente() {
        Usuario nuevoUsuario = new Usuario("Ana Gomez", new Email("ana@example.com"), new Password("Password123!"));
        gateway.guardar(nuevoUsuario);

        // Usando auth gateway method
        var authRecuperado = gateway.buscarPorEmail(new com.openlib.market.domain.autenticacion.Email("ana@example.com"));
        assertTrue(authRecuperado.isPresent());
        assertEquals("ana@example.com", authRecuperado.get().getEmail().getDireccion());
    }

    @Test
    void debeLanzarExcepcionAlGuardarEmailDuplicado() {
        Usuario usuario1 = new Usuario("Carlos 1", new Email("duplicado@example.com"), new Password("Password123!"));
        gateway.guardar(usuario1);

        Usuario usuario2 = new Usuario("Carlos 2", new Email("duplicado@example.com"), new Password("Password123!"));
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gateway.guardar(usuario2);
            // Forza flush para que salte la excepcion de Constraint si Hibernate la postergo
            repository.flush();
        });
        
        assertEquals("El email ya se encuentra registrado", exception.getMessage());
    }
}
