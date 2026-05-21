package com.openlib.market.domain.dashboard;

public record Tamano(int ancho, int alto) {
    public Tamano {
        if (ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException("El tamaño debe ser mayor a cero");
        }
    }
}
