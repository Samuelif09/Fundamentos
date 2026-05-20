package com.openlib.market.domain.antifraude;

public record RiesgoTransaccion(int valor) {
    public RiesgoTransaccion {
        if (valor < 0 || valor > 100) {
            throw new IllegalArgumentException("El riesgo debe estar entre 0 y 100");
        }
    }
}
