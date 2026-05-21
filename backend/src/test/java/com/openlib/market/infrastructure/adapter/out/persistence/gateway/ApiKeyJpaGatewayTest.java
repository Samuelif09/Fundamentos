package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.api.CredencialApi;
import com.openlib.market.domain.api.EstadoLlave;
import com.openlib.market.infrastructure.adapter.out.persistence.PersistenceTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@ActiveProfiles("test")
public class ApiKeyJpaGatewayTest {

    @Autowired
    private ApiKeyJpaGateway gateway;

    @Test
    public void testGuardarYBuscarPorValorDeLlave() {
        CredencialApi credencial = new CredencialApi("vendedor-api-1", "MiApp");
        gateway.guardar(credencial);

        String valorLlave = credencial.getLlave().valor();
        Optional<CredencialApi> recuperada = gateway.buscarPorLlave(valorLlave);

        assertTrue(recuperada.isPresent());
        assertEquals(EstadoLlave.ACTIVA, recuperada.get().getEstado());
        assertEquals("MiApp", recuperada.get().getNombreApp());
        assertEquals("vendedor-api-1", recuperada.get().getIdPropietario());
    }

    @Test
    public void testRevocacionDeApiKey() {
        CredencialApi credencial = new CredencialApi("vendedor-api-2", "AppRevocable");
        gateway.guardar(credencial);

        // Verificar estado inicial ACTIVA
        assertEquals(EstadoLlave.ACTIVA, gateway.buscarPorId(credencial.getId()).get().getEstado());

        // Revocar
        credencial.revocar();
        gateway.guardar(credencial);

        // Verificar estado REVOCADA sin duplicados
        Optional<CredencialApi> revocada = gateway.buscarPorId(credencial.getId());
        assertTrue(revocada.isPresent());
        assertEquals(EstadoLlave.REVOCADA, revocada.get().getEstado());

        // Buscar por llave sigue funcionando pero estado es REVOCADA
        Optional<CredencialApi> porLlave = gateway.buscarPorLlave(credencial.getLlave().valor());
        assertTrue(porLlave.isPresent());
        assertEquals(EstadoLlave.REVOCADA, porLlave.get().getEstado());
    }

    @Test
    public void testBuscarLlaveInexistenteRetornaVacio() {
        Optional<CredencialApi> resultado = gateway.buscarPorLlave("llave-que-no-existe-en-bd-12345678");
        assertFalse(resultado.isPresent());
    }
}
