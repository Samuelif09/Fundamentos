package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.reembolso.EstadoReembolso;
import com.openlib.market.domain.reembolso.SolicitudReembolso;
import com.openlib.market.infrastructure.adapter.out.persistence.PersistenceTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@ActiveProfiles("test")
public class ReembolsoJpaGatewayTest {

    @Autowired
    private ReembolsoJpaGateway gateway;

    @Test
    public void testGuardarYRecuperarReembolso() {
        SolicitudReembolso solicitud = new SolicitudReembolso(
                "pedido-r1", 50.0, "Libro defectuoso", null);
        gateway.guardar(solicitud);

        Optional<SolicitudReembolso> recuperada = gateway.obtenerPorId(solicitud.getId());

        assertTrue(recuperada.isPresent());
        assertEquals(EstadoReembolso.PENDIENTE, recuperada.get().getEstado());
        assertEquals(50.0, recuperada.get().getMontoReembolso(), 0.001);
        assertEquals("Libro defectuoso", recuperada.get().getMotivo());
    }

    @Test
    public void testCambioDeEstadoSinDuplicados() {
        SolicitudReembolso solicitud = new SolicitudReembolso(
                "pedido-r2", 30.0, "Error en la descarga", null);
        gateway.guardar(solicitud);

        long countAntes = gateway.listarTodas().size();

        // Aprobar el reembolso
        solicitud.aprobar();
        gateway.actualizar(solicitud);

        long countDespues = gateway.listarTodas().size();

        // Sin duplicados
        assertEquals(countAntes, countDespues);

        // Estado actualizado correctamente
        SolicitudReembolso actualizado = gateway.obtenerPorId(solicitud.getId()).get();
        assertEquals(EstadoReembolso.APROBADO, actualizado.getEstado());
    }

    @Test
    public void testListarTodasLasContenidas() {
        gateway.guardar(new SolicitudReembolso("pedido-r3", 10.0, "Motivo A", null));
        gateway.guardar(new SolicitudReembolso("pedido-r4", 20.0, "Motivo B", null));

        List<SolicitudReembolso> todas = gateway.listarTodas();

        assertTrue(todas.size() >= 2);
    }
}
