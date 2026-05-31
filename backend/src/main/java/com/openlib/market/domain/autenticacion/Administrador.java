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
    private final java.util.List<RolAdmin> roles;

    public Administrador(String id, Email email, String hashContrasena, Rol rol) {
        if (rol != Rol.ROLE_ADMIN) {
            throw new AccesoDenegadoException();
        }
        this.id = id;
        this.email = email;
        this.hashContrasena = hashContrasena;
        this.rol = rol;
        this.roles = new java.util.ArrayList<>();
    }

    public Administrador(String id, Email email, String hashContrasena, Rol rol, java.util.List<RolAdmin> roles) {
        if (rol != Rol.ROLE_ADMIN) {
            throw new AccesoDenegadoException();
        }
        this.id = id;
        this.email = email;
        this.hashContrasena = hashContrasena;
        this.rol = rol;
        this.roles = roles != null ? new java.util.ArrayList<>(roles) : new java.util.ArrayList<>();
    }

    public String getId() { return id; }
    public Email getEmail() { return email; }
    public String getHashContrasena() { return hashContrasena; }
    public Rol getRol() { return rol; }
    public java.util.List<RolAdmin> getRoles() { return java.util.List.copyOf(roles); }

    /** Convierte a UsuarioAuth para reutilizar el generador de tokens existente. */
    public UsuarioAuth comoUsuarioAuth() {
        return new UsuarioAuth(id, email, hashContrasena, "ADMIN");
    }

    public boolean esSuperAdmin() {
        return roles.stream().anyMatch(r -> r.getNombre() == NombreRolAdmin.SUPERADMIN);
    }

    public void asignarRol(RolAdmin nuevoRol) {
        if (roles.stream().noneMatch(r -> r.getNombre() == nuevoRol.getNombre())) {
            roles.add(nuevoRol);
        }
    }

    public void removerRol(NombreRolAdmin nombreRol, int cantidadSuperAdminsRestantesEnSistema) {
        if (nombreRol == NombreRolAdmin.SUPERADMIN && esSuperAdmin() && cantidadSuperAdminsRestantesEnSistema <= 1) {
            throw new ValidacionJerarquiaException("No se puede remover el rol SUPERADMIN porque es el único en el sistema.");
        }
        roles.removeIf(r -> r.getNombre() == nombreRol);
    }
}
