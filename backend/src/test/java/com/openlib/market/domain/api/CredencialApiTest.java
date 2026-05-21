package com.openlib.market.domain.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CredencialApiTest {

    @Test
    void debeGenerarLlaveActiva() {
        CredencialApi credencial = new CredencialApi("admin-1", "App Externa");
        
        assertEquals(EstadoLlave.ACTIVA, credencial.getEstado());
        assertNotNull(credencial.getLlave().valor());
        assertTrue(credencial.getLlave().valor().length() >= 32);
    }

    @Test
    void debeRevocarLlaveActiva() {
        CredencialApi credencial = new CredencialApi("admin-1", "App Externa");
        credencial.revocar();
        
        assertEquals(EstadoLlave.REVOCADA, credencial.getEstado());
    }

    @Test
    void noDebeRevocarLlaveYaRevocada() {
        CredencialApi credencial = new CredencialApi("admin-1", "App Externa");
        credencial.revocar();
        
        assertThrows(IllegalStateException.class, credencial::revocar);
    }

    @Test
    void debeRechazarApiKeyCorta() {
        assertThrows(IllegalArgumentException.class, () -> new ApiKey("corta"));
    }
}
