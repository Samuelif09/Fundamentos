package com.openlib.market.infrastructure.reembolso;

import com.openlib.market.application.reembolso.IGestionarReembolsosUseCase;
import com.openlib.market.application.reembolso.ReembolsoDto;
import com.openlib.market.domain.reembolso.MontoReembolsoInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/ventas/reembolsos")
public class ReembolsoAdminController {

    private final IGestionarReembolsosUseCase gestionarReembolsosUseCase;

    public ReembolsoAdminController(IGestionarReembolsosUseCase gestionarReembolsosUseCase) {
        this.gestionarReembolsosUseCase = gestionarReembolsosUseCase;
    }

    @PostMapping
    public ResponseEntity<?> solicitarReembolso(@RequestBody SolicitarReembolsoRequest request) {
        try {
            ReembolsoDto dto = gestionarReembolsosUseCase.solicitarReembolso(request.idPedido(), request.monto(), request.motivo());
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (MontoReembolsoInvalidoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<String> aprobarReembolso(@PathVariable String id) {
        try {
            gestionarReembolsosUseCase.aprobarReembolso(id);
            return ResponseEntity.ok("Reembolso aprobado exitosamente.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/denegar")
    public ResponseEntity<String> denegarReembolso(@PathVariable String id) {
        try {
            gestionarReembolsosUseCase.denegarReembolso(id);
            return ResponseEntity.ok("Reembolso denegado.");
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public record SolicitarReembolsoRequest(String idPedido, double monto, String motivo) {}
}
