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

    public AdminDashboardController(IVerDashboardMetricasUseCase verDashboardMetricasUseCase) {
        this.verDashboardMetricasUseCase = verDashboardMetricasUseCase;
    }

    @GetMapping("/kpis")
    public ResponseEntity<AdminKpiDto> getKpis() {
        // En un caso real, cada métrica se obtiene de un Gateway o Interactor.
        // Dado que la UI requiere estos cuatro valores simultáneamente,
        // combinaremos valores simulados con consultas si estuvieran disponibles.
        
        AdminKpiDto kpis = new AdminKpiDto(
                1250,      // totalUsers
                12,        // pendingSellers (Curaduría)
                840,       // totalBooks
                25600.50   // platformRevenue
        );
        return ResponseEntity.ok(kpis);
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
