package com.openlib.market.domain.autenticacion;

public class Email {
    private final String direccion;

    public Email(String direccion) {
        if (direccion == null || direccion.trim().isEmpty() || !direccion.contains("@")) {
            throw new IllegalArgumentException("Formato de email inválido");
        }
        this.direccion = direccion.trim().toLowerCase();
    }

    public String getDireccion() {
        return direccion;
    }


}
