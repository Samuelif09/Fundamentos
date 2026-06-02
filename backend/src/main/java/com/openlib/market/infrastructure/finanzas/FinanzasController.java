package com.openlib.market.infrastructure.finanzas;

import com.openlib.market.application.finanzas.IVerFinanzasUseCase;
import com.openlib.market.application.finanzas.ReporteFinanzasDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/vendedores")
public class FinanzasController {

    private final IVerFinanzasUseCase verFinanzasUseCase;
    private final com.openlib.market.application.finanzas.IVerDesgloseFinanzasUseCase verDesgloseUseCase;
    private final com.openlib.market.application.finanzas.IExportarVentasUseCase exportarVentasUseCase;
    private final com.openlib.market.application.finanzas.IVerGraficasVentasUseCase verGraficasVentasUseCase;
    private final com.openlib.market.application.finanzas.ISolicitarRetiroFinanzasUseCase solicitarRetiroUseCase;
    private final com.openlib.market.application.finanzas.ObtenerTransaccionesBilleteraInteractor obtenerTransaccionesBilleteraInteractor;

    public FinanzasController(
            IVerFinanzasUseCase verFinanzasUseCase,
            com.openlib.market.application.finanzas.IVerDesgloseFinanzasUseCase verDesgloseUseCase,
            com.openlib.market.application.finanzas.IExportarVentasUseCase exportarVentasUseCase,
            com.openlib.market.application.finanzas.IVerGraficasVentasUseCase verGraficasVentasUseCase,
            com.openlib.market.application.finanzas.ISolicitarRetiroFinanzasUseCase solicitarRetiroUseCase,
            com.openlib.market.application.finanzas.ObtenerTransaccionesBilleteraInteractor obtenerTransaccionesBilleteraInteractor) {
        this.verFinanzasUseCase = verFinanzasUseCase;
        this.verDesgloseUseCase = verDesgloseUseCase;
        this.exportarVentasUseCase = exportarVentasUseCase;
        this.verGraficasVentasUseCase = verGraficasVentasUseCase;
        this.solicitarRetiroUseCase = solicitarRetiroUseCase;
        this.obtenerTransaccionesBilleteraInteractor = obtenerTransaccionesBilleteraInteractor;
    }

    @GetMapping("/{idVendedor}/finanzas/ingresos")
    public ResponseEntity<ReporteFinanzasDto> obtenerIngresos(
            @PathVariable String idVendedor,
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        
        ReporteFinanzasDto reporte = verFinanzasUseCase.obtenerReporteIngresos(idVendedor, desde, hasta);
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/{idVendedor}/finanzas/transacciones")
    public ResponseEntity<java.util.List<com.openlib.market.application.finanzas.TransactionDto>> obtenerTransaccionesBilletera(
            @PathVariable String idVendedor) {
        return ResponseEntity.ok(obtenerTransaccionesBilleteraInteractor.obtenerTransacciones(idVendedor));
    }

    @GetMapping("/{idVendedor}/finanzas/exportar")
    public ResponseEntity<byte[]> exportarVentas(
            @PathVariable String idVendedor,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(defaultValue = "csv") String formato) {
        
        com.openlib.market.domain.finanzas.ReporteExportable reporte = exportarVentasUseCase.exportar(idVendedor, desde, hasta, formato);
        
        String contentType = "csv".equalsIgnoreCase(formato) ? "text/csv" : "application/vnd.ms-excel";
        
        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + reporte.getNombreArchivo() + "\"")
                .header(org.springframework.http.HttpHeaders.CONTENT_TYPE, contentType)
                .body(reporte.getContenido());
    }

    @GetMapping("/{idVendedor}/metricas/ventas")
    public ResponseEntity<java.util.List<com.openlib.market.domain.finanzas.PuntoDatos>> verGraficasVentas(
            @PathVariable String idVendedor,
            @RequestParam(defaultValue = "DIARIO") String intervalo) {
        try {
            com.openlib.market.domain.finanzas.IntervaloTiempo enumIntervalo = com.openlib.market.domain.finanzas.IntervaloTiempo.valueOf(intervalo.toUpperCase());
            java.util.List<com.openlib.market.domain.finanzas.PuntoDatos> serie = verGraficasVentasUseCase.verGrafica(idVendedor, enumIntervalo);
            return ResponseEntity.ok(serie);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{idVendedor}/finanzas/retiros")
    public ResponseEntity<Void> solicitarRetiro(
            @PathVariable String idVendedor,
            @RequestBody RetiroRequest req) {
        try {
            solicitarRetiroUseCase.solicitarRetiro(idVendedor, req.monto(), req.cuentaDestino());
            return ResponseEntity.ok().build();
        } catch (com.openlib.market.domain.finanzas.FondosInsuficientesException e) {
            return ResponseEntity.status(409).build(); // Conflict
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public record RetiroRequest(double monto, String cuentaDestino) {}
}
