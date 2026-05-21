package com.openlib.market.domain.carrito;

public class Subtotal {
    private final double valor;

    public Subtotal(double valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("El subtotal no puede ser negativo");
        }
        this.valor = valor;
    }

    public double getValor() { return valor; }
}
