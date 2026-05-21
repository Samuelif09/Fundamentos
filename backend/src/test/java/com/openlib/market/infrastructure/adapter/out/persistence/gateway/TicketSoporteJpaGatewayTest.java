package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.soporte.*;
import com.openlib.market.infrastructure.adapter.out.persistence.PersistenceTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@ActiveProfiles("test")
public class TicketSoporteJpaGatewayTest {

    @Autowired
    private TicketSoporteJpaGateway gateway;

    @Test
    public void testListadoPaginadoOrdenadoPorPrioridad() {
        LocalDateTime ahora = LocalDateTime.now();
        // 3 tickets: BAJA, ALTA, MEDIA — deben retornar en orden ALTA, MEDIA, BAJA
        gateway.guardar(new TicketSoporte(UUID.randomUUID().toString(), "u1", "Asunto Baja",   "Desc", EstadoTicket.ABIERTO, Prioridad.BAJA,  ahora.minusHours(3)));
        gateway.guardar(new TicketSoporte(UUID.randomUUID().toString(), "u2", "Asunto Alta",   "Desc", EstadoTicket.ABIERTO, Prioridad.ALTA,  ahora.minusHours(2)));
        gateway.guardar(new TicketSoporte(UUID.randomUUID().toString(), "u3", "Asunto Media",  "Desc", EstadoTicket.ABIERTO, Prioridad.MEDIA, ahora.minusHours(1)));
        // Ticket CERRADO — no debe aparecer
        gateway.guardar(new TicketSoporte(UUID.randomUUID().toString(), "u4", "Asunto Cerrado","Desc", EstadoTicket.CERRADO, Prioridad.ALTA, ahora));

        List<TicketSoporte> resultado = gateway.listarPorEstados(
                List.of(EstadoTicket.ABIERTO, EstadoTicket.EN_PROGRESO), 0, 10);

        assertEquals(3, resultado.size(), "Solo deben listarse tickets ABIERTO/EN_PROGRESO");
        assertEquals(Prioridad.ALTA,  resultado.get(0).getPrioridad(), "El primero debe ser ALTA");
        assertEquals(Prioridad.MEDIA, resultado.get(1).getPrioridad(), "El segundo debe ser MEDIA");
        assertEquals(Prioridad.BAJA,  resultado.get(2).getPrioridad(), "El tercero debe ser BAJA");
    }

    @Test
    public void testPaginacion() {
        LocalDateTime ahora = LocalDateTime.now();
        for (int i = 0; i < 5; i++) {
            gateway.guardar(new TicketSoporte(UUID.randomUUID().toString(), "u-pag", "Ticket " + i,
                    "Desc", EstadoTicket.ABIERTO, Prioridad.MEDIA, ahora.minusMinutes(i)));
        }

        List<TicketSoporte> pagina0 = gateway.listarPorEstados(List.of(EstadoTicket.ABIERTO), 0, 3);
        List<TicketSoporte> pagina1 = gateway.listarPorEstados(List.of(EstadoTicket.ABIERTO), 1, 3);

        assertEquals(3, pagina0.size(), "Página 0 debe traer 3");
        assertEquals(2, pagina1.size(), "Página 1 debe traer 2 restantes");
    }
}
