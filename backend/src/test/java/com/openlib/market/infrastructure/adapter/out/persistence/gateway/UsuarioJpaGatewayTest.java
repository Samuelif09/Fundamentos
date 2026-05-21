package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.registro.Email;
import com.openlib.market.domain.registro.Password;
import com.openlib.market.domain.registro.Usuario;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.UsuarioMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import com.openlib.market.infrastructure.adapter.out.persistence.PersistenceTestConfig;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@ActiveProfiles("test")
public class UsuarioJpaGatewayTest {

    @Autowired
    private UsuarioJpaGateway usuarioJpaGateway;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testGuardarYBuscarUsuarioPorId() {
        Usuario usuario = new Usuario("Juan Perez", new Email("juan@example.com"), new Password("Password123!"));
        
        // Guardar
        usuarioJpaGateway.guardar(usuario);

        // Buscar
        Optional<Usuario> guardado = usuarioJpaGateway.buscarPorId(usuario.getId());

        assertTrue(guardado.isPresent());
        assertEquals("Juan Perez", guardado.get().getNombre());
        assertEquals("juan@example.com", guardado.get().getEmail().getValor());
    }

    @Test
    public void testBuscarPorEmail() {
        Usuario usuario = new Usuario("Maria", new Email("maria@test.com"), new Password("Password123!"));
        usuarioJpaGateway.guardar(usuario);

        assertTrue(usuarioJpaGateway.existeEmail(new Email("maria@test.com")));
        assertFalse(usuarioJpaGateway.existeEmail(new Email("noexiste@test.com")));
    }

    @Test
    public void testExcepcionEmailDuplicado() {
        Usuario usuario1 = new Usuario("Pedro", new Email("pedro@test.com"), new Password("Password123!"));
        Usuario usuario2 = new Usuario("Otro Pedro", new Email("pedro@test.com"), new Password("Password456!"));

        usuarioJpaGateway.guardar(usuario1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioJpaGateway.guardar(usuario2);
        });

        assertEquals("El email ya se encuentra registrado", exception.getMessage());
    }
}
