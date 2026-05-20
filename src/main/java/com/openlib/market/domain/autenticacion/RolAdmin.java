package com.openlib.market.domain.autenticacion;

import java.util.List;

public class RolAdmin {
    private final NombreRolAdmin nombre;
    private final List<String> permisos;

    public RolAdmin(NombreRolAdmin nombre, List<String> permisos) {
        if (nombre == null) throw new IllegalArgumentException("El nombre del rol es obligatorio");
        this.nombre = nombre;
        this.permisos = permisos != null ? permisos : List.of();
    }

    public NombreRolAdmin getNombre() { return nombre; }
    public List<String> getPermisos() { return permisos; }
}
