package com.openlib.market.domain.afiliado;

import java.util.UUID;

public class CodigoRastreo {
    private final String valor;

    public CodigoRastreo() {
        this.valor = UUID.randomUUID().toString();
    }

    public CodigoRastreo(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de rastreo no puede estar vacío");
        }
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
