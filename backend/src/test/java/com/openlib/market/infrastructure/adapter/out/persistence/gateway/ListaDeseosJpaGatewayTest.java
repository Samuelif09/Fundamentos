package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.infrastructure.adapter.out.persistence.PersistenceTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@ActiveProfiles("test")
public class ListaDeseosJpaGatewayTest {

    @Autowired
    private ListaDeseosJpaGateway gateway;

    @Test
    public void testGuardarYRecuperarWishlist() {
        gateway.guardar("user-wish-1", Set.of("isbn-1", "isbn-2"));

        Set<String> isbns = gateway.obtenerPorUsuario("user-wish-1");
        assertEquals(2, isbns.size());
        assertTrue(isbns.contains("isbn-1"));
        assertTrue(isbns.contains("isbn-2"));

        List<String> interesados = gateway.obtenerUsuariosInteresados("isbn-2");
        assertTrue(interesados.contains("user-wish-1"));
    }
}
