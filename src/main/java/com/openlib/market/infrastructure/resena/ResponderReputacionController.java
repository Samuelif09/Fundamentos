package com.openlib.market.infrastructure.resena;

import com.openlib.market.application.resena.IResponderReputacionUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/resenas")
public class ResponderReputacionController {

    private final IResponderReputacionUseCase responderReputacionUseCase;

    public ResponderReputacionController(IResponderReputacionUseCase responderReputacionUseCase) {
        this.responderReputacionUseCase = responderReputacionUseCase;
    }

    @PostMapping("/{idResena}/respuesta")
    public ResponseEntity<Void> responder(
            @PathVariable String idResena,
            @RequestBody RespuestaRequest req) {
        try {
            responderReputacionUseCase.responder(req.idVendedor(), idResena, req.comentario());
            return ResponseEntity.ok().build();
        } catch (com.openlib.market.domain.resena.RespuestaDuplicadaException e) {
            return ResponseEntity.status(409).build(); // Conflict
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).build(); // Forbidden
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public record RespuestaRequest(String idVendedor, String comentario) {}
}
