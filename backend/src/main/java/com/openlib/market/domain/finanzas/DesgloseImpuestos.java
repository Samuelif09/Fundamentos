package com.openlib.market.domain.finanzas;

public class DesgloseImpuestos {
    private final double subtotal;
    private final double iva; // 19%
    private final double total;

    public DesgloseImpuestos(double subtotal) {
        if (subtotal < 0) throw new IllegalArgumentException("El subtotal no puede ser negativo");
        this.subtotal = subtotal;
        this.iva = Math.round((subtotal * 0.19) * 100.0) / 100.0;
        this.total = this.subtotal + this.iva;
    }

    public double getSubtotal() { return subtotal; }
    public double getIva() { return iva; }
    public double getTotal() { return total; }
}
