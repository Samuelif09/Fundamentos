package com.openlib.market.domain.autenticacion;

public class UsuarioAuth {
    private final String id;
    private final Email email;
    private final String hashContrasena;
    private final String rol;
    private final String estadoCuenta;

    public UsuarioAuth(String id, Email email, String hashContrasena) {
        this(id, email, hashContrasena, "COMPRADOR", "ACTIVO");
    }

    public UsuarioAuth(String id, Email email, String hashContrasena, String rol) {
        this(id, email, hashContrasena, rol, "ACTIVO");
    }

    public UsuarioAuth(String id, Email email, String hashContrasena, String rol, String estadoCuenta) {
        this.id = id;
        this.email = email;
        this.hashContrasena = hashContrasena;
        this.rol = rol;
        this.estadoCuenta = estadoCuenta != null ? estadoCuenta : "ACTIVO";
    }

    public String getId() { return id; }
    public Email getEmail() { return email; }
    public String getHashContrasena() { return hashContrasena; }
    public String getRol() { return rol; }
    public String getEstadoCuenta() { return estadoCuenta; }
}
