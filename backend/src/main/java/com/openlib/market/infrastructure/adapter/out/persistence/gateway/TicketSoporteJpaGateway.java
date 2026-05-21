package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.soporte.*;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.TicketSoporteEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.TicketSoporteRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("ticketSoporteJpaGateway")
@Primary
public class TicketSoporteJpaGateway implements ITicketSoporteGateway {

    private final TicketSoporteRepository repository;

    public TicketSoporteJpaGateway(TicketSoporteRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TicketSoporte> listarPorEstados(List<EstadoTicket> estados, int page, int size) {
        List<String> estadosStr = estados.stream().map(Enum::name).collect(Collectors.toList());
        return repository.findByEstadoInOrdenado(estadosStr, PageRequest.of(page, size))
                .stream().map(this::toDomain).collect(Collectors.toList());
    }

    public void guardar(TicketSoporte ticket) {
        repository.save(toEntity(ticket));
    }

    private TicketSoporte toDomain(TicketSoporteEntity e) {
        return new TicketSoporte(e.getId(), e.getIdUsuario(), e.getAsunto(), e.getDescripcion(),
                EstadoTicket.valueOf(e.getEstado()), Prioridad.valueOf(e.getPrioridad()), e.getFechaCreacion());
    }

    private TicketSoporteEntity toEntity(TicketSoporte t) {
        return new TicketSoporteEntity(t.getId(), t.getIdUsuario(), t.getAsunto(), t.getDescripcion(),
                t.getEstado().name(), t.getPrioridad().name(), t.getFechaCreacion());
    }
}
