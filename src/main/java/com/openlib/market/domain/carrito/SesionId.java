package com.openlib.market.domain.carrito;

public class SesionId {
    private final String valor;

    public SesionId(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de sesión no puede ser nulo o vacío");
        }
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
