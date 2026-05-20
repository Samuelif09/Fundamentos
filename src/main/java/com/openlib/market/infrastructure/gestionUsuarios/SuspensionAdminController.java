package com.openlib.market.infrastructure.gestionUsuarios;

import com.openlib.market.application.gestionUsuarios.ISuspenderGestionUsuariosUseCase;
import com.openlib.market.domain.registro.EstadoInvalidoException;
import com.openlib.market.domain.registro.MotivoSuspension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/usuarios")
public class SuspensionAdminController {

    private final ISuspenderGestionUsuariosUseCase suspenderUseCase;

    public SuspensionAdminController(ISuspenderGestionUsuariosUseCase suspenderUseCase) {
        this.suspenderUseCase = suspenderUseCase;
    }

    @PatchMapping("/{userId}/suspender")
    public ResponseEntity<String> suspenderUsuario(@PathVariable String userId, @RequestBody SuspensionRequest request) {
        try {
            MotivoSuspension motivo = new MotivoSuspension(request.motivo());
            suspenderUseCase.suspenderUsuario(userId, motivo);
            return ResponseEntity.ok("Usuario suspendido correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EstadoInvalidoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public record SuspensionRequest(String motivo) {}
}
