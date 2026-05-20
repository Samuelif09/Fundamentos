package com.openlib.market.infrastructure.configuracion;

import com.openlib.market.application.configuracion.ComisionDto;
import com.openlib.market.application.configuracion.IConfigurarComisionesUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/configuracion/comisiones")
public class ComisionAdminController {

    private final IConfigurarComisionesUseCase configurarComisionesUseCase;

    public ComisionAdminController(IConfigurarComisionesUseCase configurarComisionesUseCase) {
        this.configurarComisionesUseCase = configurarComisionesUseCase;
    }

    @PostMapping
    public ResponseEntity<String> configurarComision(@RequestBody ConfigurarComisionRequest request) {
        try {
            configurarComisionesUseCase.configurarComision(request.idCategoria(), request.porcentajeComision());
            return ResponseEntity.ok("Regla de comisión configurada exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{idCategoria}")
    public ResponseEntity<?> obtenerComisionParaCategoria(@PathVariable String idCategoria) {
        try {
            ComisionDto dto = configurarComisionesUseCase.obtenerComisionParaCategoria(idCategoria);
            return ResponseEntity.ok(dto);
        } catch (IllegalStateException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<ComisionDto>> listarComisiones() {
        return ResponseEntity.ok(configurarComisionesUseCase.listarComisiones());
    }

    public record ConfigurarComisionRequest(String idCategoria, double porcentajeComision) {}
}
