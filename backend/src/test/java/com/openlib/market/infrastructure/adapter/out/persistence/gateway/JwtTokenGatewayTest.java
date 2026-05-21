package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.autenticacion.Email;
import com.openlib.market.domain.autenticacion.TokenAcceso;
import com.openlib.market.domain.autenticacion.UsuarioAuth;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenGatewayTest {

    @Test
    public void testGenerarTokenSimulado() {
        JwtTokenGateway gateway = new JwtTokenGateway();
        UsuarioAuth usuario = new UsuarioAuth("user123", new Email("test@test.com"), "hash");

        TokenAcceso token = gateway.generar(usuario);

        assertNotNull(token);
        assertNotNull(token.getToken());
        assertTrue(token.getToken().startsWith("jwt-hmac-sha256.user123."));
    }
}
