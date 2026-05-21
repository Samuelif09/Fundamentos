package com.openlib.market.domain.registro;

public class Password {
    private final String valor;

    public Password(String valor) {
        if (valor == null || valor.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }
        if (!valor.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("La contraseña debe tener al menos una mayúscula");
        }
        if (!valor.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("La contraseña debe tener al menos un número");
        }
        this.valor = valor;
    }

    // Constructor privado para crear instancias sin validación (ej. valores ya hasheados)
    private Password(String valor, boolean omitirValidacion) {
        this.valor = valor;
    }

    // Factory method: crea un Password desde un hash ya procesado, sin re-validar reglas de negocio
    public static Password desdeHash(String hash) {
        return new Password(hash, true);
    }

    public String getValor() {
        return valor;
    }
}
