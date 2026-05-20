package com.openlib.market.domain.soporte;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TicketSoporteTest {

    @Test
    void debeCrearTicketCorrectamente() {
        TicketSoporte ticket = new TicketSoporte("t1", "u1", "Problema con pago", "Descripción detallada", EstadoTicket.ABIERTO, Prioridad.ALTA, LocalDateTime.now());
        assertEquals("t1", ticket.getId());
        assertEquals(EstadoTicket.ABIERTO, ticket.getEstado());
        assertEquals(Prioridad.ALTA, ticket.getPrioridad());
    }

    @Test
    void debeLanzarExcepcionSiAsuntoEsVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> new TicketSoporte("t1", "u1", "  ", "Desc", EstadoTicket.ABIERTO, Prioridad.ALTA, LocalDateTime.now()));
    }
}
