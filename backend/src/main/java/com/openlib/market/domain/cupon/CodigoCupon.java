package com.openlib.market.domain.cupon;

public class CodigoCupon {
    private final String valor;

    public CodigoCupon(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del cupón no puede estar vacío");
        }
        this.valor = valor.trim().toUpperCase();
    }

    public String getValor() { return valor; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodigoCupon c)) return false;
        return valor.equals(c.valor);
    }

    @Override
    public int hashCode() { return valor.hashCode(); }
}
