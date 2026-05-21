package com.openlib.market.domain.dashboard;

public record Posicion(int x, int y) {
    public Posicion {
        if (x < 0 || y < 0) {
            throw new IllegalArgumentException("Las coordenadas no pueden ser negativas");
        }
    }
}
