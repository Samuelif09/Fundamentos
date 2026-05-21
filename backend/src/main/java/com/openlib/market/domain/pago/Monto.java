package com.openlib.market.domain.pago;

public class Monto {
    private final double valor;

    public Monto(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        this.valor = valor;
    }

    public double getValor() { return valor; }
}
