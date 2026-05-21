package com.openlib.market.domain.detalle;

public class DuracionEnMinutos {
    private final int valor;

    public DuracionEnMinutos(int valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a 0 minutos");
        }
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
