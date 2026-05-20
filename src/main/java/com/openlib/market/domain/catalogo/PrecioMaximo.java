package com.openlib.market.domain.catalogo;

public class PrecioMaximo {
    private final double valor;

    public PrecioMaximo(double valor) {
        if (valor < 0) throw new IllegalArgumentException("El precio máximo no puede ser negativo");
        this.valor = valor;
    }

    public double getValor() { return valor; }
}
