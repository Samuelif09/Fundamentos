package com.openlib.market.infrastructure.soporte;

import com.openlib.market.application.soporte.IVerSoporteUseCase;
import com.openlib.market.application.soporte.TicketSoporteDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/soporte")
public class SoporteAdminController {

    private final IVerSoporteUseCase verSoporteUseCase;

    public SoporteAdminController(IVerSoporteUseCase verSoporteUseCase) {
        this.verSoporteUseCase = verSoporteUseCase;
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketSoporteDto>> listarTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<TicketSoporteDto> tickets = verSoporteUseCase.listarTicketsAbiertos(page, size);
        return ResponseEntity.ok(tickets);
    }

    @PatchMapping("/disputas/{id}/mediar")
    public ResponseEntity<?> iniciarMediacion(@PathVariable String id, @org.springframework.beans.factory.annotation.Autowired com.openlib.market.application.soporte.IGestionarSoporteUseCase gestionarSoporteUseCase) {
        try {
            com.openlib.market.application.soporte.DisputaDto dto = gestionarSoporteUseCase.iniciarMediacion(id);
            return ResponseEntity.ok(dto);
        } catch (com.openlib.market.domain.soporte.TransicionEstadoInvalidaException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/disputas/{id}/resolver")
    public ResponseEntity<?> resolverDisputa(@PathVariable String id, @RequestBody ResolverDisputaRequest request, @org.springframework.beans.factory.annotation.Autowired com.openlib.market.application.soporte.IGestionarSoporteUseCase gestionarSoporteUseCase) {
        try {
            com.openlib.market.application.soporte.DisputaDto dto = gestionarSoporteUseCase.resolverDisputa(id, request.resolucion());
            return ResponseEntity.ok(dto);
        } catch (com.openlib.market.domain.soporte.TransicionEstadoInvalidaException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public record ResolverDisputaRequest(String resolucion) {}
}
