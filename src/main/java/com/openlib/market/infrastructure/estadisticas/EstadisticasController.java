package com.openlib.market.infrastructure.estadisticas;

import com.openlib.market.application.estadisticas.IVerEstadisticasMiCuentaUseCase;
import com.openlib.market.domain.estadisticas.EstadisticaLector;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
public class EstadisticasController {

    private final IVerEstadisticasMiCuentaUseCase estadisticasUseCase;

    public EstadisticasController(IVerEstadisticasMiCuentaUseCase estadisticasUseCase) {
        this.estadisticasUseCase = estadisticasUseCase;
    }

    @GetMapping("/{id}/estadisticas")
    public ResponseEntity<EstadisticaLector> obtenerEstadisticas(@PathVariable String id) {
        EstadisticaLector estadisticas = estadisticasUseCase.obtenerEstadisticas(id);
        return ResponseEntity.ok(estadisticas);
    }
}
