package com.openlib.market.domain.finanzas;

public class MontoRetiro {
    private final double valor;

    public MontoRetiro(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("El monto de retiro debe ser mayor a 0");
        }
        this.valor = valor;
    }

    public double getValor() { return valor; }
}
