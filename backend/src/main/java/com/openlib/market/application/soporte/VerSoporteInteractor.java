package com.openlib.market.application.soporte;

import com.openlib.market.domain.soporte.EstadoTicket;
import com.openlib.market.domain.soporte.ITicketSoporteGateway;
import com.openlib.market.domain.soporte.TicketSoporte;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class VerSoporteInteractor implements IVerSoporteUseCase {

    private final ITicketSoporteGateway ticketGateway;

    public VerSoporteInteractor(ITicketSoporteGateway ticketGateway) {
        this.ticketGateway = ticketGateway;
    }

    @Override
    public List<TicketSoporteDto> listarTicketsAbiertos(int page, int size) {
        List<TicketSoporte> tickets = ticketGateway.listarPorEstados(
                List.of(EstadoTicket.ABIERTO, EstadoTicket.EN_PROGRESO), page, size);

        // Ordenar: ALTA primero, luego por antigüedad (más viejo primero)
        return tickets.stream()
                .sorted(Comparator.comparingInt((TicketSoporte t) -> t.getPrioridad().ordinal())
                        .thenComparing(TicketSoporte::getFechaCreacion))
                .map(t -> new TicketSoporteDto(
                        t.getId(),
                        t.getIdUsuario(),
                        t.getAsunto(),
                        t.getDescripcion(),
                        t.getEstado().name(),
                        t.getPrioridad().name(),
                        t.getFechaCreacion() != null ? t.getFechaCreacion().toString() : "N/A"
                ))
                .collect(Collectors.toList());
    }
}
