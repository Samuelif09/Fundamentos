package com.openlib.market.infrastructure.autenticacion;

import com.openlib.market.application.autenticacion.IGestionarRolesUseCase;
import com.openlib.market.domain.autenticacion.ValidacionJerarquiaException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/usuarios")
public class AdminRolesController {

    private final IGestionarRolesUseCase gestionarRolesUseCase;

    public AdminRolesController(IGestionarRolesUseCase gestionarRolesUseCase) {
        this.gestionarRolesUseCase = gestionarRolesUseCase;
    }

    @PutMapping("/{adminId}/roles/asignar")
    public ResponseEntity<String> asignarRol(@PathVariable String adminId, @RequestBody AsignarRolRequest request) {
        try {
            gestionarRolesUseCase.asignarRol(adminId, request.nombreRol(), request.permisos());
            return ResponseEntity.ok("Rol asignado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{adminId}/roles/remover")
    public ResponseEntity<String> removerRol(@PathVariable String adminId, @RequestBody RemoverRolRequest request) {
        try {
            gestionarRolesUseCase.removerRol(adminId, request.nombreRol());
            return ResponseEntity.ok("Rol removido correctamente");
        } catch (ValidacionJerarquiaException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public record AsignarRolRequest(String nombreRol, List<String> permisos) {}
    public record RemoverRolRequest(String nombreRol) {}
}
