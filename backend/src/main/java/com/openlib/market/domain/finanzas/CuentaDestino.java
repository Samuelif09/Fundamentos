package com.openlib.market.domain.finanzas;

public class CuentaDestino {
    private final String valor;

    public CuentaDestino(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("La cuenta destino es obligatoria");
        }
        this.valor = valor;
    }

    public String getValor() { return valor; }
}
