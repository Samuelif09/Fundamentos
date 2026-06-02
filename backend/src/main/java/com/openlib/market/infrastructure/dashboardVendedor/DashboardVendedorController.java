package com.openlib.market.infrastructure.dashboardVendedor;

import com.openlib.market.application.dashboardVendedor.ObtenerFinanzasVendedorInteractor;
import com.openlib.market.application.dashboardVendedor.ObtenerMetricasVendedorInteractor;
import com.openlib.market.application.dashboardVendedor.VendedorFinanzasDto;
import com.openlib.market.application.dashboardVendedor.VendedorMetricasDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vendedores")
public class DashboardVendedorController {

    private final ObtenerFinanzasVendedorInteractor finanzasInteractor;
    private final ObtenerMetricasVendedorInteractor metricasInteractor;

    public DashboardVendedorController(ObtenerFinanzasVendedorInteractor finanzasInteractor, ObtenerMetricasVendedorInteractor metricasInteractor) {
        this.finanzasInteractor = finanzasInteractor;
        this.metricasInteractor = metricasInteractor;
    }

    @GetMapping("/{id}/dashboard/finanzas")
    public ResponseEntity<VendedorFinanzasDto> obtenerFinanzas(@PathVariable("id") String idVendedor) {
        try {
            VendedorFinanzasDto finanzas = finanzasInteractor.obtenerFinanzas(idVendedor);
            return ResponseEntity.ok(finanzas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/dashboard/metricas")
    public ResponseEntity<VendedorMetricasDto> obtenerMetricas(@PathVariable("id") String idVendedor) {
        try {
            VendedorMetricasDto metricas = metricasInteractor.obtenerMetricas(idVendedor);
            return ResponseEntity.ok(metricas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
