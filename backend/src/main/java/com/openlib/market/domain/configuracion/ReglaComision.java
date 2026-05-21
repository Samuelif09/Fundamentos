package com.openlib.market.domain.configuracion;

import java.util.UUID;

public class ReglaComision {
    private final String id;
    private final String idCategoria;
    private final double porcentajeComision;

    public ReglaComision(String idCategoria, double porcentajeComision) {
        if (idCategoria == null || idCategoria.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de categoría es obligatorio (use 'GLOBAL' para la regla por defecto)");
        }
        if (porcentajeComision < 0 || porcentajeComision > 100) {
            throw new IllegalArgumentException("El porcentaje de comisión debe estar entre 0 y 100");
        }

        this.id = UUID.randomUUID().toString();
        this.idCategoria = idCategoria.toUpperCase();
        this.porcentajeComision = porcentajeComision;
    }

    public ReglaComision(String id, String idCategoria, double porcentajeComision) {
        this.id = id;
        this.idCategoria = idCategoria.toUpperCase();
        this.porcentajeComision = porcentajeComision;
    }

    public String getId() { return id; }
    public String getIdCategoria() { return idCategoria; }
    public double getPorcentajeComision() { return porcentajeComision; }

    public double calcularComision(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        return monto * (porcentajeComision / 100.0);
    }
}
