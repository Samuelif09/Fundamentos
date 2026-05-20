package com.openlib.market.domain.autenticacion;

public class UsuarioAuth {
    private final String id;
    private final Email email;
    private final String hashContrasena;

    public UsuarioAuth(String id, Email email, String hashContrasena) {
        this.id = id;
        this.email = email;
        this.hashContrasena = hashContrasena;
    }

    public String getId() { return id; }
    public Email getEmail() { return email; }
    public String getHashContrasena() { return hashContrasena; }
}
