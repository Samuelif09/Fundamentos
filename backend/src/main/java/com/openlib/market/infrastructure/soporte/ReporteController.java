package com.openlib.market.infrastructure.soporte;

import com.openlib.market.application.soporte.IReportarSoporteUseCase;
import com.openlib.market.application.soporte.ReportarSoporteRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/reportes")
public class ReporteController {

    private final IReportarSoporteUseCase reportarSoporteUseCase;

    public ReporteController(IReportarSoporteUseCase reportarSoporteUseCase) {
        this.reportarSoporteUseCase = reportarSoporteUseCase;
    }

    @PostMapping
    public ResponseEntity<String> reportarContenido(@RequestBody ReportarSoporteRequestDto request) {
        reportarSoporteUseCase.reportar(request);
        return ResponseEntity.ok("Reporte enviado exitosamente y está pendiente de revisión.");
    }
}
