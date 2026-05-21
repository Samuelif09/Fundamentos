package com.openlib.market.domain.notificacion;

public class EmailDestino {
    private final String email;

    public EmailDestino(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("El email no es válido para enviar el recibo");
        }
        this.email = email;
    }

    public String getEmail() { return email; }
}
