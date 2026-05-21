package com.openlib.market.infrastructure.dashboardGlobal;

import com.openlib.market.application.dashboardGlobal.IVerDashboardMetricasUseCase;
import com.openlib.market.application.dashboardGlobal.SerieGraficaDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/dashboard/graficas")
public class DashboardMetricasController {

    private final IVerDashboardMetricasUseCase metricasUseCase;

    public DashboardMetricasController(IVerDashboardMetricasUseCase metricasUseCase) {
        this.metricasUseCase = metricasUseCase;
    }

    @GetMapping("/ventas")
    public ResponseEntity<SerieGraficaDto> obtenerGraficaVentas(
            @RequestParam(defaultValue = "MENSUAL") String intervalo,
            @RequestParam(defaultValue = "2026") int anio) {
        
        try {
            SerieGraficaDto dto = metricasUseCase.generarGraficaVentas(intervalo, anio);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
