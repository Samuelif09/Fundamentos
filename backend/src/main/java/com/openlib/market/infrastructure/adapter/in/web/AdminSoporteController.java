package com.openlib.market.infrastructure.adapter.in.web;

import com.openlib.market.application.soporte.IVerSoporteUseCase;
import com.openlib.market.application.soporte.TicketSoporteDto;
import com.openlib.market.infrastructure.adapter.in.web.dto.SupportTicketDto;
import com.openlib.market.infrastructure.adapter.in.web.dto.TicketReplyRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/admin/soporte/tickets")
public class AdminSoporteController {

    private final IVerSoporteUseCase verSoporteUseCase;

    public AdminSoporteController(IVerSoporteUseCase verSoporteUseCase) {
        this.verSoporteUseCase = verSoporteUseCase;
    }

    @GetMapping
    public ResponseEntity<List<SupportTicketDto>> getTickets() {
        List<TicketSoporteDto> backendTickets = verSoporteUseCase.listarTicketsAbiertos(0, 50);

        List<SupportTicketDto> frontendTickets = backendTickets.stream()
                .map(t -> new SupportTicketDto(
                        t.getId(),
                        t.getIdUsuario(), // Mapeamos userId a email temporalmente para la vista
                        t.getAsunto(),
                        t.getDescripcion(),
                        t.getPrioridad(),
                        t.getEstado(),
                        t.getFechaCreacion()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(frontendTickets);
    }

    @PostMapping("/{id}/responder")
    public ResponseEntity<String> replyToTicket(@PathVariable String id, @RequestBody TicketReplyRequestDto request) {
        // En un caso real, inyectaríamos un IGestionarSoporteUseCase o algo similar
        // que manejara respuestas a tickets. Por ahora simulamos la acción exitosa.
        return ResponseEntity.ok("Respuesta enviada al usuario correctamente.");
    }

    @PostMapping("/{id}/cerrar")
    public ResponseEntity<String> closeTicket(@PathVariable String id) {
        // Simulación de cerrado exitoso.
        return ResponseEntity.ok("Ticket cerrado exitosamente.");
    }
}
