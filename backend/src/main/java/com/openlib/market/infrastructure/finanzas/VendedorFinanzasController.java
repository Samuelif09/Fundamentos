package com.openlib.market.infrastructure.finanzas;
import com.openlib.market.application.finanzas.GenerarReporteIngresosInteractor;
import com.openlib.market.application.finanzas.IngresosVendedorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
@RestController
@RequestMapping("/api/v1/vendedores/{idVendedor}/finanzas")
public class VendedorFinanzasController {
    private final GenerarReporteIngresosInteractor generarReporteIngresosInteractor;
    private final com.openlib.market.application.finanzas.GenerarMetricasVentasVendedorInteractor generarMetricasVentasVendedorInteractor;

    public VendedorFinanzasController(GenerarReporteIngresosInteractor generarReporteIngresosInteractor,
                                      com.openlib.market.application.finanzas.GenerarMetricasVentasVendedorInteractor generarMetricasVentasVendedorInteractor) {
        this.generarReporteIngresosInteractor = generarReporteIngresosInteractor;
        this.generarMetricasVentasVendedorInteractor = generarMetricasVentasVendedorInteractor;
    }

    @GetMapping("/ingresos")
    public ResponseEntity<IngresosVendedorResponseDto> reporteIngresos(
            @PathVariable String idVendedor,
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta) {
        return ResponseEntity.ok(generarReporteIngresosInteractor.ejecutar(idVendedor, desde, hasta));
    }

    @GetMapping("/metricas")
    public ResponseEntity<java.util.List<com.openlib.market.application.finanzas.MetricaTemporalResponseDto>> metricasVendedor(
            @PathVariable String idVendedor,
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta,
            @RequestParam String agrupacion) {
        com.openlib.market.domain.finanzas.AgrupacionTiempo agrupacionEnum = com.openlib.market.domain.finanzas.AgrupacionTiempo.valueOf(agrupacion.toUpperCase());
        return ResponseEntity.ok(generarMetricasVentasVendedorInteractor.ejecutar(idVendedor, desde, hasta, agrupacionEnum));
    }
}
