package com.openlib.market.application.finanzas;

import java.time.LocalDate;

public class ReporteFinanzasDto {
    private final String idVendedor;
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final double ingresosTotales;

    public ReporteFinanzasDto(String idVendedor, LocalDate fechaInicio, LocalDate fechaFin, double ingresosTotales) {
        this.idVendedor = idVendedor;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.ingresosTotales = ingresosTotales;
    }

    public String getIdVendedor() { return idVendedor; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public double getIngresosTotales() { return ingresosTotales; }
}
