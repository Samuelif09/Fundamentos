package com.openlib.market.application.soporte;

import com.openlib.market.domain.soporte.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerSoporteInteractorTest {

    private ITicketSoporteGateway ticketGateway;
    private VerSoporteInteractor interactor;

    @BeforeEach
    void setUp() {
        ticketGateway = mock(ITicketSoporteGateway.class);
        interactor = new VerSoporteInteractor(ticketGateway);
    }

    @Test
    void debeOrdenarPorPrioridadYAntiguedad() {
        LocalDateTime ahora = LocalDateTime.now();
        TicketSoporte baja = new TicketSoporte("t1", "u1", "Baja prioridad", "Desc", EstadoTicket.ABIERTO, Prioridad.BAJA, ahora.minusDays(5));
        TicketSoporte alta1 = new TicketSoporte("t2", "u2", "Alta vieja", "Desc", EstadoTicket.ABIERTO, Prioridad.ALTA, ahora.minusDays(3));
        TicketSoporte alta2 = new TicketSoporte("t3", "u3", "Alta nueva", "Desc", EstadoTicket.EN_PROGRESO, Prioridad.ALTA, ahora.minusDays(1));
        TicketSoporte media = new TicketSoporte("t4", "u4", "Media prioridad", "Desc", EstadoTicket.ABIERTO, Prioridad.MEDIA, ahora.minusDays(2));

        when(ticketGateway.listarPorEstados(anyList(), eq(0), eq(20)))
                .thenReturn(List.of(baja, alta1, alta2, media));

        List<TicketSoporteDto> result = interactor.listarTicketsAbiertos(0, 20);

        assertEquals(4, result.size());
        // ALTA primero (ordinal 0), luego MEDIA (ordinal 1), luego BAJA (ordinal 2)
        assertEquals("ALTA", result.get(0).getPrioridad());
        assertEquals("ALTA", result.get(1).getPrioridad());
        // Dentro de ALTA: más viejo primero
        assertEquals("t2", result.get(0).getId());
        assertEquals("t3", result.get(1).getId());
        assertEquals("MEDIA", result.get(2).getPrioridad());
        assertEquals("BAJA", result.get(3).getPrioridad());
    }

    @Test
    void debeRetornarListaVaciaSiNoHayTickets() {
        when(ticketGateway.listarPorEstados(anyList(), eq(0), eq(20)))
                .thenReturn(List.of());

        List<TicketSoporteDto> result = interactor.listarTicketsAbiertos(0, 20);
        assertTrue(result.isEmpty());
    }
}
