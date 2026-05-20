package com.openlib.market.domain.detalle;

public class Isbn {
    private final String valor;

    public Isbn(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN no puede estar vacío");
        }
        this.valor = valor.trim();
    }

    public String getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Isbn isbn = (Isbn) o;
        return valor.equals(isbn.valor);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(valor);
    }
}
