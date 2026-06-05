package com.openlib.market.application.finanzas;

import java.math.BigDecimal;

public class MetricaTemporalResponseDto {
    private String periodo;
    private int cantidadItemsVendidos;
    private BigDecimal ingresoNetoVendedor;

    public MetricaTemporalResponseDto(String periodo, int cantidadItemsVendidos, BigDecimal ingresoNetoVendedor) {
        this.periodo = periodo;
        this.cantidadItemsVendidos = cantidadItemsVendidos;
        this.ingresoNetoVendedor = ingresoNetoVendedor;
    }

    public String getPeriodo() {
        return periodo;
    }

    public int getCantidadItemsVendidos() {
        return cantidadItemsVendidos;
    }

    public BigDecimal getIngresoNetoVendedor() {
        return ingresoNetoVendedor;
    }
}
