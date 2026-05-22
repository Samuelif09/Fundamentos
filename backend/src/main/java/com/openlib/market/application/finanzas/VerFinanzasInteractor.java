package com.openlib.market.application.finanzas;

import com.openlib.market.domain.finanzas.CalculadoraIngresosDomainService;
import com.openlib.market.domain.finanzas.ILiquidacionGateway;
import com.openlib.market.domain.finanzas.Periodo;
import com.openlib.market.domain.finanzas.ReporteFinanciero;
import com.openlib.market.domain.finanzas.TransaccionFinanciera;

import java.time.LocalDate;
import java.util.List;

public class VerFinanzasInteractor implements IVerFinanzasUseCase {

    private final ILiquidacionGateway liquidacionGateway;
    private final CalculadoraIngresosDomainService calculadora;

    public VerFinanzasInteractor(ILiquidacionGateway liquidacionGateway, CalculadoraIngresosDomainService calculadora) {
        this.liquidacionGateway = liquidacionGateway;
        this.calculadora = calculadora;
    }

    @Override
    public ReporteFinanzasDto obtenerReporteIngresos(String idVendedor, LocalDate inicio, LocalDate fin) {
        Periodo periodo = new Periodo(inicio, fin);
        
        List<TransaccionFinanciera> transacciones = liquidacionGateway.obtenerTransaccionesPorVendedor(idVendedor);
        
        double totalIngresos = calculadora.calcularIngresos(transacciones, periodo);
        
        ReporteFinanciero reporte = new ReporteFinanciero(idVendedor, periodo, totalIngresos);

        return new ReporteFinanzasDto(
                reporte.getIdVendedor(),
                reporte.getPeriodo().getInicio(),
                reporte.getPeriodo().getFin(),
                reporte.getTotalIngresos()
        );
    }
}
