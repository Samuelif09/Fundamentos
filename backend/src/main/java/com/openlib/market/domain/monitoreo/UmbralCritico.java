package com.openlib.market.domain.monitoreo;

public record UmbralCritico(double valor) {
    public UmbralCritico {
        if (valor < 0) {
            throw new IllegalArgumentException("El umbral no puede ser negativo");
        }
    }
}
