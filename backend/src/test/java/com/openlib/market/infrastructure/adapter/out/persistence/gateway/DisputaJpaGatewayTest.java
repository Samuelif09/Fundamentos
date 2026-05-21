package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.soporte.*;
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
public class DisputaJpaGatewayTest {

    @Autowired
    private DisputaJpaGateway gateway;

    @Test
    public void testCicloDeVidaCompleto() {
        // Crear disputa ABIERTA
        Disputa disputa = new Disputa("pedido-d1", "comprador-1", "vendedor-1", "Libro no llegó");
        gateway.guardar(disputa);

        // Verificar estado inicial
        Optional<Disputa> recuperada = gateway.buscarPorId(disputa.getId());
        assertTrue(recuperada.isPresent());
        assertEquals(EstadoDisputa.ABIERTA, recuperada.get().getEstado());
        assertEquals(Resolucion.PENDIENTE, recuperada.get().getResolucion());

        // Iniciar mediación
        disputa.iniciarMediacion();
        gateway.guardar(disputa);
        assertEquals(EstadoDisputa.EN_MEDIACION, gateway.buscarPorId(disputa.getId()).get().getEstado());

        // Resolver a favor del comprador
        disputa.resolver(Resolucion.FAVOR_COMPRADOR);
        gateway.guardar(disputa);

        Disputa final_ = gateway.buscarPorId(disputa.getId()).get();
        assertEquals(EstadoDisputa.RESUELTA, final_.getEstado());
        assertEquals(Resolucion.FAVOR_COMPRADOR, final_.getResolucion());
    }

    @Test
    public void testGuardadoSinDuplicados() {
        Disputa d = new Disputa("pedido-d2", "c2", "v2", "Producto defectuoso");
        gateway.guardar(d);
        gateway.guardar(d); // segunda llamada = upsert

        // Sigue siendo 1 registro, no 2
        assertTrue(gateway.buscarPorId(d.getId()).isPresent());
    }
}
