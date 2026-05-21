package com.openlib.market.domain.afiliado;

public class PorcentajeComisionAfiliado {
    private final double valor;

    public PorcentajeComisionAfiliado(double valor) {
        if (valor < 0 || valor > 100) {
            throw new IllegalArgumentException("La comisión del afiliado debe estar entre 0 y 100%");
        }
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }
}
