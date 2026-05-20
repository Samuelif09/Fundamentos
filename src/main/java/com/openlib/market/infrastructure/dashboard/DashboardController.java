package com.openlib.market.infrastructure.dashboard;

import com.openlib.market.application.dashboard.IVerMetricasDashboardUseCase;
import com.openlib.market.domain.dashboard.DashboardKpi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para A-02: Dashboard de KPIs del administrador.
 * Endpoint: GET /api/v1/admin/dashboard/kpis
 *
 * Nota: En Entrega 2 se añadirá @PreAuthorize("hasRole('ADMIN')")
 * con Spring Security configurado.
 */
@RestController
@RequestMapping("/api/v1/admin/dashboard")
public class DashboardController {

    private final IVerMetricasDashboardUseCase verMetricasUseCase;

    public DashboardController(IVerMetricasDashboardUseCase verMetricasUseCase) {
        this.verMetricasUseCase = verMetricasUseCase;
    }

    @GetMapping("/kpis")
    public ResponseEntity<DashboardKpi> obtenerKpis() {
        DashboardKpi kpis = verMetricasUseCase.obtenerKpisDelDia();
        return ResponseEntity.ok(kpis);
    }
}
