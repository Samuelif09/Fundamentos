package com.openlib.market.infrastructure.adapter.in.web;

import com.openlib.market.application.dashboardGlobal.IVerDashboardMetricasUseCase;
import com.openlib.market.application.dashboardGlobal.SerieGraficaDto;
import com.openlib.market.infrastructure.adapter.in.web.dto.AdminChartDataDto;
import com.openlib.market.infrastructure.adapter.in.web.dto.AdminKpiDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
public class AdminDashboardController {

    private final IVerDashboardMetricasUseCase verDashboardMetricasUseCase;
    private final com.openlib.market.application.dashboardGlobal.ObtenerKpisDashboardInteractor obtenerKpisDashboardInteractor;

    public AdminDashboardController(IVerDashboardMetricasUseCase verDashboardMetricasUseCase,
                                    com.openlib.market.application.dashboardGlobal.ObtenerKpisDashboardInteractor obtenerKpisDashboardInteractor) {
        this.verDashboardMetricasUseCase = verDashboardMetricasUseCase;
        this.obtenerKpisDashboardInteractor = obtenerKpisDashboardInteractor;
    }

    @GetMapping("/kpis")
    public ResponseEntity<AdminKpiDto> getKpis() {
        return ResponseEntity.ok(obtenerKpisDashboardInteractor.ejecutar());
    }

    @GetMapping("/graficas")
    public ResponseEntity<AdminChartDataDto> getGraficas() {
        // Obtenemos los ingresos del interactor existente (ej: año 2026, mes a mes)
        SerieGraficaDto ingresosSerie = verDashboardMetricasUseCase.generarGraficaVentas("MENSUAL", 2026);
        
        List<AdminChartDataDto.DataPoint> revenueGrowth = new ArrayList<>();
        if (ingresosSerie != null && ingresosSerie.getPuntos() != null) {
            for (Map<String, Object> punto : ingresosSerie.getPuntos()) {
                String etiqueta = (String) punto.get("etiqueta");
                double valor = (Double) punto.get("valor");
                revenueGrowth.add(new AdminChartDataDto.DataPoint(etiqueta, valor));
            }
        } else {
            // Mock fallback if empty
            revenueGrowth.add(new AdminChartDataDto.DataPoint("Ene", 1000));
            revenueGrowth.add(new AdminChartDataDto.DataPoint("Feb", 2500));
            revenueGrowth.add(new AdminChartDataDto.DataPoint("Mar", 1800));
        }

        // Mock growth para usuarios, ya que IVerDashboardMetricasUseCase solo genera ventas.
        List<AdminChartDataDto.DataPoint> userGrowth = new ArrayList<>();
        userGrowth.add(new AdminChartDataDto.DataPoint("Ene", 50));
        userGrowth.add(new AdminChartDataDto.DataPoint("Feb", 120));
        userGrowth.add(new AdminChartDataDto.DataPoint("Mar", 300));

        AdminChartDataDto chartData = new AdminChartDataDto(userGrowth, revenueGrowth);
        return ResponseEntity.ok(chartData);
    }
}
