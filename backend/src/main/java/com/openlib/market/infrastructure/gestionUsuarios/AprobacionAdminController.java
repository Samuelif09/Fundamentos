package com.openlib.market.infrastructure.gestionUsuarios;

import com.openlib.market.application.gestionUsuarios.IAprobarGestionUsuariosUseCase;
import com.openlib.market.domain.vendedor.SolicitudInvalidaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/vendedores")
public class AprobacionAdminController {

    private final IAprobarGestionUsuariosUseCase aprobarUseCase;

    public AprobacionAdminController(IAprobarGestionUsuariosUseCase aprobarUseCase) {
        this.aprobarUseCase = aprobarUseCase;
    }

    @PostMapping("/{sellerId}/aprobar")
    public ResponseEntity<String> aprobarVendedor(@PathVariable String sellerId) {
        try {
            aprobarUseCase.aprobarVendedor(sellerId);
            return ResponseEntity.ok("Vendedor aprobado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SolicitudInvalidaException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
