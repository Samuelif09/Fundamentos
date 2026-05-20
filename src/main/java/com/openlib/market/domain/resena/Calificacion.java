package com.openlib.market.domain.resena;

public class Calificacion {
    private final int valor;

    public Calificacion(int valor) {
        if (valor < 1 || valor > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5");
        }
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
