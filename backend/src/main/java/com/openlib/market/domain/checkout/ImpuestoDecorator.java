package com.openlib.market.domain.checkout;

public class ImpuestoDecorator implements CalculadorPrecio {
    private final CalculadorPrecio calculadorBase;
    private final double porcentajeImpuesto;

    public ImpuestoDecorator(CalculadorPrecio calculadorBase, double porcentajeImpuesto) {
        this.calculadorBase = calculadorBase;
        this.porcentajeImpuesto = porcentajeImpuesto;
    }

    @Override
    public double calcularTotal() {
        double base = calculadorBase.calcularTotal();
        return base + (base * porcentajeImpuesto);
    }
}
