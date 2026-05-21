package com.openlib.market.infrastructure.reporte;

import com.openlib.market.application.reporte.IExportarDashboardMetricasUseCase;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/dashboard/reportes")
public class ReportesController {

    private final IExportarDashboardMetricasUseCase exportarUseCase;

    public ReportesController(IExportarDashboardMetricasUseCase exportarUseCase) {
        this.exportarUseCase = exportarUseCase;
    }

    @GetMapping("/exportar")
    public ResponseEntity<byte[]> exportarReporte(
            @RequestParam(defaultValue = "VENTAS") String tipo,
            @RequestParam(defaultValue = "CSV") String formato) {
        try {
            byte[] archivo = exportarUseCase.exportarReporte(tipo, formato);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "reporte_" + tipo.toLowerCase() + "." + formato.toLowerCase());
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(archivo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
