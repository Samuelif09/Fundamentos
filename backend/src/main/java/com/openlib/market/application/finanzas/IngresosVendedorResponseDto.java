package com.openlib.market.application.finanzas;
import java.math.BigDecimal;
public class IngresosVendedorResponseDto {
    private BigDecimal totalVentasBrutas;
    private BigDecimal totalComisionesPlataforma;
    private BigDecimal ingresoNetoVendedor;
    public IngresosVendedorResponseDto(BigDecimal totalVentasBrutas, BigDecimal totalComisionesPlataforma, BigDecimal ingresoNetoVendedor) {
        this.totalVentasBrutas = totalVentasBrutas;
        this.totalComisionesPlataforma = totalComisionesPlataforma;
        this.ingresoNetoVendedor = ingresoNetoVendedor;
    }
    public BigDecimal getTotalVentasBrutas() { return totalVentasBrutas; }
    public BigDecimal getTotalComisionesPlataforma() { return totalComisionesPlataforma; }
    public BigDecimal getIngresoNetoVendedor() { return ingresoNetoVendedor; }
}
