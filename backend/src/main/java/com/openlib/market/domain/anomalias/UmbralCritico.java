package com.openlib.market.domain.anomalias;

public record UmbralCritico(double valor) {
    public UmbralCritico {
        if (valor < 0) {
            throw new IllegalArgumentException("El valor del umbral crítico no puede ser negativo");
        }
    }
}
