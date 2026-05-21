package com.openlib.market.domain.cupon;

public class DescuentoPorcentaje implements EstrategiaDescuento {
    private final double porcentaje;

    public DescuentoPorcentaje(double porcentaje) {
        if (porcentaje <= 0 || porcentaje > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 1 y 100");
        }
        this.porcentaje = porcentaje;
    }

    @Override
    public double aplicar(double totalOriginal) {
        return totalOriginal * (1 - porcentaje / 100.0);
    }

    public double getPorcentaje() { return porcentaje; }
}
