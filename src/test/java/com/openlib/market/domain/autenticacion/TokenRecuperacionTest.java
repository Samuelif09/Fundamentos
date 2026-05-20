package com.openlib.market.domain.autenticacion;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TokenRecuperacionTest {

    @Test
    void debeInformarQueEstaExpiradoSiLaFechaActualPaso() {
        LocalDateTime expiracion = LocalDateTime.now().minusMinutes(5);
        TokenRecuperacion token = new TokenRecuperacion("token-123", expiracion);
        
        assertTrue(token.estaExpirado(LocalDateTime.now()));
    }

    @Test
    void noDebeEstarExpiradoSiLaFechaActualNoHaPasado() {
        LocalDateTime expiracion = LocalDateTime.now().plusMinutes(5);
        TokenRecuperacion token = new TokenRecuperacion("token-123", expiracion);
        
        assertFalse(token.estaExpirado(LocalDateTime.now()));
    }

    @Test
    void generarNuevoDebeCrearTokenValidoPorQuinceMinutos() {
        TokenRecuperacion token = TokenRecuperacion.generarNuevo();
        
        assertNotNull(token.getValor());
        assertFalse(token.estaExpirado(LocalDateTime.now()));
    }
}
