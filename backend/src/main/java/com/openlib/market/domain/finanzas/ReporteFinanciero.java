package com.openlib.market.domain.finanzas;

public class ReporteFinanciero {
    private final String idVendedor;
    private final Periodo periodo;
    private final double totalIngresos;

    public ReporteFinanciero(String idVendedor, Periodo periodo, double totalIngresos) {
        this.idVendedor = idVendedor;
        this.periodo = periodo;
        this.totalIngresos = totalIngresos;
    }

    public String getIdVendedor() { return idVendedor; }
    public Periodo getPeriodo() { return periodo; }
    public double getTotalIngresos() { return totalIngresos; }
}
