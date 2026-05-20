package com.openlib.market.application.configuracion;

public class ComisionDto {
    private final String idCategoria;
    private final double porcentajeComision;

    public ComisionDto(String idCategoria, double porcentajeComision) {
        this.idCategoria = idCategoria;
        this.porcentajeComision = porcentajeComision;
    }

    public String getIdCategoria() { return idCategoria; }
    public double getPorcentajeComision() { return porcentajeComision; }
}
