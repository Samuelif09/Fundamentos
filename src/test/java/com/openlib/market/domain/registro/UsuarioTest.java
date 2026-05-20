package com.openlib.market.domain.registro;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void debeCrearUsuarioSiDatosSonValidos() {
        Email email = new Email("test@test.com");
        Password pass = new Password("Secret123!");
        Usuario usuario = new Usuario("Juan", email, pass);

        assertNotNull(usuario);
        assertEquals("Juan", usuario.getNombre());
        assertEquals("test@test.com", usuario.getEmail().getValor());
    }

    @Test
    void debeLanzarExcepcionSiEmailEsInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new Email("test"));
        assertThrows(IllegalArgumentException.class, () -> new Email("test@"));
        assertThrows(IllegalArgumentException.class, () -> new Email(null));
    }

    @Test
    void debeLanzarExcepcionSiPasswordEsInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new Password("123")); // corto
        assertThrows(IllegalArgumentException.class, () -> new Password("abcdefgh")); // sin mayuscula ni numero
        assertThrows(IllegalArgumentException.class, () -> new Password("Abcdefgh")); // sin numero
        assertThrows(IllegalArgumentException.class, () -> new Password(null));
    }
}
