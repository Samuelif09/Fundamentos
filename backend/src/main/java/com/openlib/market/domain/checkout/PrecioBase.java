package com.openlib.market.domain.checkout;

public class PrecioBase implements CalculadorPrecio {
    private final double monto;

    public PrecioBase(double monto) {
        this.monto = monto;
    }

    @Override
    public double calcularTotal() {
        return this.monto;
    }
}
