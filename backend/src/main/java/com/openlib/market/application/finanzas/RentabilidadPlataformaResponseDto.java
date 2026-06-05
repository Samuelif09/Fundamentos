package com.openlib.market.application.finanzas;

import java.math.BigDecimal;

public class RentabilidadPlataformaResponseDto {
    private BigDecimal totalVentasBrutas;
    private BigDecimal totalComisionesPlataforma;
    private BigDecimal totalPagadoAVendedores;

    public RentabilidadPlataformaResponseDto(BigDecimal totalVentasBrutas, BigDecimal totalComisionesPlataforma, BigDecimal totalPagadoAVendedores) {
        this.totalVentasBrutas = totalVentasBrutas;
        this.totalComisionesPlataforma = totalComisionesPlataforma;
        this.totalPagadoAVendedores = totalPagadoAVendedores;
    }

    public BigDecimal getTotalVentasBrutas() {
        return totalVentasBrutas;
    }

    public BigDecimal getTotalComisionesPlataforma() {
        return totalComisionesPlataforma;
    }

    public BigDecimal getTotalPagadoAVendedores() {
        return totalPagadoAVendedores;
    }
}
