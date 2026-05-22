package com.openlib.market.infrastructure.dashboard;

import com.openlib.market.application.dashboard.ConfiguracionDashboardDto;
import com.openlib.market.application.dashboard.IPersonalizarDashboardMetricasUseCase;
import com.openlib.market.application.dashboard.WidgetDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/admin/{id}/preferencias/dashboard")
public class DashboardConfigAdminController {

    private final IPersonalizarDashboardMetricasUseCase personalizarDashboardUseCase;

    public DashboardConfigAdminController(IPersonalizarDashboardMetricasUseCase personalizarDashboardUseCase) {
        this.personalizarDashboardUseCase = personalizarDashboardUseCase;
    }

    @GetMapping
    public ResponseEntity<ConfiguracionDashboardDto> obtenerPreferencias(@PathVariable String id) {
        return ResponseEntity.ok(personalizarDashboardUseCase.obtenerPreferencias(id));
    }

    @PutMapping
    public ResponseEntity<?> guardarPreferencias(@PathVariable String id, @RequestBody List<WidgetDto> widgets) {
        try {
            ConfiguracionDashboardDto dto = personalizarDashboardUseCase.guardarPreferencias(id, widgets);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
