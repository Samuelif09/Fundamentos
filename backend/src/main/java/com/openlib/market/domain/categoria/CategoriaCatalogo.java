package com.openlib.market.domain.categoria;

import java.util.UUID;

public class CategoriaCatalogo {
    private final String id;
    private NombreCategoria nombre;
    private EstadoCategoria estado;

    public CategoriaCatalogo(NombreCategoria nombre) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.estado = EstadoCategoria.ACTIVA;
    }

    public CategoriaCatalogo(String id, NombreCategoria nombre, EstadoCategoria estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }

    public String getId() { return id; }
    public NombreCategoria getNombre() { return nombre; }
    public EstadoCategoria getEstado() { return estado; }

    public void editarNombre(NombreCategoria nuevoNombre) {
        if (nuevoNombre == null) {
            throw new IllegalArgumentException("El nuevo nombre no puede ser nulo");
        }
        this.nombre = nuevoNombre;
    }

    public void activar() {
        this.estado = EstadoCategoria.ACTIVA;
    }

    public void desactivar() {
        this.estado = EstadoCategoria.INACTIVA;
    }
}
