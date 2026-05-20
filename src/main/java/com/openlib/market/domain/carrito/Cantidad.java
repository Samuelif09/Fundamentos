package com.openlib.market.domain.carrito;

public class Cantidad {
    private final int valor;

    public Cantidad(int valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    public Cantidad sumar(Cantidad otra) {
        return new Cantidad(this.valor + otra.getValor());
    }
}
