package com.openlib.market.domain.curaduria;

public record ScoreToxicidad(double valor) {
    public ScoreToxicidad {
        if (valor < 0.0 || valor > 1.0) {
            throw new IllegalArgumentException("El score debe estar entre 0.0 y 1.0");
        }
    }
}
