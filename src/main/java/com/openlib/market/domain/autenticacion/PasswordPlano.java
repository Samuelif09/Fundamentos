package com.openlib.market.domain.autenticacion;

public class PasswordPlano {
    private final String valor;

    public PasswordPlano(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
