package com.openlib.market.domain.busqueda;

public class PalabraClave {
    private final String valor;

    public PalabraClave(String valor) {
        if (valor == null || valor.trim().length() < 3) {
            throw new IllegalArgumentException("La palabra clave debe tener al menos 3 caracteres.");
        }
        if (valor.matches(".*[;\\-%=<>].*")) {
            throw new IllegalArgumentException("La palabra clave contiene caracteres no permitidos.");
        }
        this.valor = valor.trim().toLowerCase();
    }

    public String getValor() {
        return valor;
    }
}
