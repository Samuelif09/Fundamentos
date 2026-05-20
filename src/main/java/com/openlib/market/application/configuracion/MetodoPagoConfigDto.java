package com.openlib.market.application.configuracion;

public class MetodoPagoConfigDto {
    private final String id;
    private final String nombre;
    private final String estado;

    public MetodoPagoConfigDto(String id, String nombre, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEstado() { return estado; }
}
