package com.openlib.market.domain.autenticacion;

/**
 * Agregado raíz del administrador. Hereda conceptualmente de UsuarioAuth
 * e incorpora el rol ROLE_ADMIN como restricción de dominio.
 */
public class Administrador {

    private final String id;
    private final Email email;
    private final String hashContrasena;
    private final Rol rol;

    public Administrador(String id, Email email, String hashContrasena, Rol rol) {
        if (rol != Rol.ROLE_ADMIN) {
            throw new AccesoDenegadoException();
        }
        this.id = id;
        this.email = email;
        this.hashContrasena = hashContrasena;
        this.rol = rol;
    }

    public String getId() { return id; }
    public Email getEmail() { return email; }
    public String getHashContrasena() { return hashContrasena; }
    public Rol getRol() { return rol; }

    /** Convierte a UsuarioAuth para reutilizar el generador de tokens existente. */
    public UsuarioAuth comoUsuarioAuth() {
        return new UsuarioAuth(id, email, hashContrasena);
    }
}
