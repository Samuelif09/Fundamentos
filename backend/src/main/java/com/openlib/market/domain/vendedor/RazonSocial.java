package com.openlib.market.domain.vendedor;

public class RazonSocial {
    private final String valor;

    public RazonSocial(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("La razón social no puede estar vacía");
        }
        this.valor = valor.trim();
    }

    public String getValor() {
        return valor;
    }
}
