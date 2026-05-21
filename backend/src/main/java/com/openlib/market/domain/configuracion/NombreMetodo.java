package com.openlib.market.domain.configuracion;

public class NombreMetodo {
    private final String valor;

    public NombreMetodo(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del método de pago no puede estar vacío");
        }
        this.valor = valor.trim();
    }

    public String getValor() {
        return valor;
    }
}
