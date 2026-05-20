package com.openlib.market.domain.cupon;

public class DescuentoMontoFijo implements EstrategiaDescuento {
    private final double monto;

    public DescuentoMontoFijo(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto del descuento debe ser mayor que cero");
        }
        this.monto = monto;
    }

    @Override
    public double aplicar(double totalOriginal) {
        double resultado = totalOriginal - monto;
        return Math.max(resultado, 0.0); // El total nunca puede ser negativo
    }

    public double getMonto() { return monto; }
}
