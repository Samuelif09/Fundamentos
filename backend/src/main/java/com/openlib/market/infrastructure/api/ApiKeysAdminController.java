package com.openlib.market.infrastructure.api;

import com.openlib.market.application.api.CredencialApiDto;
import com.openlib.market.application.api.IGenerarCredencialesApiUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/integraciones/api-keys")
public class ApiKeysAdminController {

    private final IGenerarCredencialesApiUseCase generarCredencialesUseCase;

    public ApiKeysAdminController(IGenerarCredencialesApiUseCase generarCredencialesUseCase) {
        this.generarCredencialesUseCase = generarCredencialesUseCase;
    }

    @PostMapping
    public ResponseEntity<CredencialApiDto> generarCredencial(@RequestBody GenerarLlaveRequest request) {
        CredencialApiDto dto = generarCredencialesUseCase.generarCredencial(request.idPropietario(), request.nombreApp());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> revocarCredencial(@PathVariable String id) {
        try {
            CredencialApiDto dto = generarCredencialesUseCase.revocarCredencial(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public record GenerarLlaveRequest(String idPropietario, String nombreApp) {}
}
