package com.openlib.market.domain.registro;

import java.util.regex.Pattern;

public class Email {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private final String valor;

    public Email(String valor) {
        if (valor == null || !EMAIL_PATTERN.matcher(valor).matches()) {
            throw new IllegalArgumentException("Formato de email inválido");
        }
        this.valor = valor.toLowerCase();
    }

    public String getValor() {
        return valor;
    }
}
