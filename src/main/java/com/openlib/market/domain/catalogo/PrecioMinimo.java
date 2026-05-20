package com.openlib.market.domain.catalogo;

public class PrecioMinimo {
    private final double valor;

    public PrecioMinimo(double valor) {
        if (valor < 0) throw new IllegalArgumentException("El precio mínimo no puede ser negativo");
        this.valor = valor;
    }

    public double getValor() { return valor; }
}
