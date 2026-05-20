package com.openlib.market.domain.configuracion;

import java.util.UUID;

public class ConfiguracionMetodoPago {
    private final String id;
    private final NombreMetodo nombre;
    private EstadoMetodoPago estado;

    public ConfiguracionMetodoPago(NombreMetodo nombre, EstadoMetodoPago estado) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.estado = estado;
    }

    public ConfiguracionMetodoPago(String id, NombreMetodo nombre, EstadoMetodoPago estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public NombreMetodo getNombre() {
        return nombre;
    }

    public EstadoMetodoPago getEstado() {
        return estado;
    }

    public void deshabilitar(int cantidadActivosActuales) {
        if (this.estado == EstadoMetodoPago.DESHABILITADO) {
            return;
        }
        if (cantidadActivosActuales <= 1) {
            throw new ConfiguracionInvalidaException("No se puede deshabilitar el último método de pago activo en la plataforma");
        }
        this.estado = EstadoMetodoPago.DESHABILITADO;
    }

    public void habilitar() {
        this.estado = EstadoMetodoPago.HABILITADO;
    }
}
