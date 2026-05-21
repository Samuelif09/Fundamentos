package com.openlib.market.domain.inventario;

public class PorcentajeDescuento {
    private final int valor;

    public PorcentajeDescuento(int valor) {
        if (valor < 1 || valor > 99) {
            throw new IllegalArgumentException("El porcentaje de descuento debe estar entre 1 y 99");
        }
        this.valor = valor;
    }

    public int getValor() { return valor; }
}
