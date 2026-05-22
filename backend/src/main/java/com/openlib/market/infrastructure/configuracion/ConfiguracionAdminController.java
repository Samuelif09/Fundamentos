package com.openlib.market.infrastructure.configuracion;

import com.openlib.market.application.configuracion.IGestionarConfiguracionSistemaUseCase;
import com.openlib.market.application.configuracion.MetodoPagoConfigDto;
import com.openlib.market.domain.configuracion.ConfiguracionInvalidaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/admin/configuracion")
public class ConfiguracionAdminController {

    private final IGestionarConfiguracionSistemaUseCase configuracionUseCase;

    public ConfiguracionAdminController(IGestionarConfiguracionSistemaUseCase configuracionUseCase) {
        this.configuracionUseCase = configuracionUseCase;
    }

    @GetMapping("/metodos-pago")
    public ResponseEntity<List<MetodoPagoConfigDto>> listarMetodosPago() {
        return ResponseEntity.ok(configuracionUseCase.listarMetodosPago());
    }

    @PatchMapping("/metodos-pago/{id}/estado")
    public ResponseEntity<String> cambiarEstadoMetodoPago(@PathVariable String id, @RequestBody CambiarEstadoRequest request) {
        try {
            configuracionUseCase.cambiarEstadoMetodoPago(id, request.estado());
            return ResponseEntity.ok("Estado del método de pago actualizado correctamente");
        } catch (ConfiguracionInvalidaException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public record CambiarEstadoRequest(String estado) {}
}
