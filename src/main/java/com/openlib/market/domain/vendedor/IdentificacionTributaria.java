package com.openlib.market.domain.vendedor;

public class IdentificacionTributaria {
    private final String valor;

    public IdentificacionTributaria(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("La identificación tributaria no puede estar vacía");
        }
        // Validación básica: Solo alfanuméricos y guiones, mínimo 5, máximo 20 caracteres
        if (!valor.matches("^[a-zA-Z0-9-]{5,20}$")) {
            throw new IllegalArgumentException("Formato de identificación tributaria inválido");
        }
        this.valor = valor.trim().toUpperCase();
    }

    public String getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentificacionTributaria that = (IdentificacionTributaria) o;
        return valor.equals(that.valor);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(valor);
    }
}
