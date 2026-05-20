package com.openlib.market.domain.pago;

public class TokenPago {
    private final String valor;

    public TokenPago(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El token de pago no puede ser nulo o vacío");
        }
        this.valor = valor;
    }

    public String getValor() { return valor; }
}
