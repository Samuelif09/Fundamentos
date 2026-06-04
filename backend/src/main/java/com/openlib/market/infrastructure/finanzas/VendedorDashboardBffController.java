package com.openlib.market.infrastructure.finanzas;

import com.openlib.market.application.finanzas.GenerarReporteIngresosInteractor;
import com.openlib.market.application.finanzas.GenerarMetricasVentasVendedorInteractor;
import com.openlib.market.application.finanzas.IngresosVendedorResponseDto;
import com.openlib.market.application.finanzas.MetricaTemporalResponseDto;
import com.openlib.market.domain.finanzas.AgrupacionTiempo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/vendedores/{idVendedor}/dashboard")
public class VendedorDashboardBffController {

    private final GenerarReporteIngresosInteractor generarReporteIngresosInteractor;
    private final GenerarMetricasVentasVendedorInteractor generarMetricasVentasVendedorInteractor;

    private final com.openlib.market.infrastructure.adapter.out.persistence.repository.VendedorRepository vendedorRepository;

    public VendedorDashboardBffController(
            GenerarReporteIngresosInteractor generarReporteIngresosInteractor,
            GenerarMetricasVentasVendedorInteractor generarMetricasVentasVendedorInteractor,
            com.openlib.market.infrastructure.adapter.out.persistence.repository.VendedorRepository vendedorRepository) {
        this.generarReporteIngresosInteractor = generarReporteIngresosInteractor;
        this.generarMetricasVentasVendedorInteractor = generarMetricasVentasVendedorInteractor;
        this.vendedorRepository = vendedorRepository;
    }

    @GetMapping("/finanzas")
    public ResponseEntity<Map<String, Object>> getFinances(@PathVariable String idVendedor) {
        String realIdVendedor = vendedorRepository.findByIdUsuario(idVendedor).map(v -> v.getId()).orElse(idVendedor);
        // Rango ampliado para atrapar todos los datos históricos en la DB H2
        LocalDate desde = LocalDate.of(2000, 1, 1);
        LocalDate hasta = LocalDate.now().plusYears(1);

        IngresosVendedorResponseDto ingresos = generarReporteIngresosInteractor.ejecutar(realIdVendedor, desde, hasta);
        List<MetricaTemporalResponseDto> metricas = generarMetricasVentasVendedorInteractor.ejecutar(realIdVendedor, desde, hasta, AgrupacionTiempo.MENSUAL);

        int totalOrders = metricas.stream().mapToInt(MetricaTemporalResponseDto::getCantidadItemsVendidos).sum();

        Map<String, Object> response = new HashMap<>();
        response.put("totalRevenue", ingresos.getTotalVentasBrutas());
        response.put("pendingBalance", ingresos.getIngresoNetoVendedor()); // Usamos ingreso neto como balance pendiente proxy
        response.put("totalOrders", totalOrders);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/metricas")
    public ResponseEntity<Map<String, Object>> getMetrics(@PathVariable String idVendedor) {
        String realIdVendedor = vendedorRepository.findByIdUsuario(idVendedor).map(v -> v.getId()).orElse(idVendedor);
        // Rango ampliado para atrapar todos los datos históricos en la DB H2
        LocalDate desde = LocalDate.of(2000, 1, 1);
        LocalDate hasta = LocalDate.now().plusYears(1);

        List<MetricaTemporalResponseDto> metricas = generarMetricasVentasVendedorInteractor.ejecutar(realIdVendedor, desde, hasta, AgrupacionTiempo.MENSUAL);

        int totalBooksSold = 0;
        Map<String, Integer> monthlySales = new HashMap<>();

        for (MetricaTemporalResponseDto metrica : metricas) {
            totalBooksSold += metrica.getCantidadItemsVendidos();
            monthlySales.put(metrica.getPeriodo(), metrica.getCantidadItemsVendidos());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalBooksSold", totalBooksSold);
        response.put("monthlySales", monthlySales);

        return ResponseEntity.ok(response);
    }
}
