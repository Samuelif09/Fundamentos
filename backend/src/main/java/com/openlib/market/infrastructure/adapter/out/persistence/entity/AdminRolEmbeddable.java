package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class AdminRolEmbeddable {
    private String nombreRol;
    private String permisosComaSeparados;

    public AdminRolEmbeddable() {}

    public AdminRolEmbeddable(String nombreRol, String permisosComaSeparados) {
        this.nombreRol = nombreRol;
        this.permisosComaSeparados = permisosComaSeparados;
    }

    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }

    public String getPermisosComaSeparados() { return permisosComaSeparados; }
    public void setPermisosComaSeparados(String permisosComaSeparados) { this.permisosComaSeparados = permisosComaSeparados; }
}
