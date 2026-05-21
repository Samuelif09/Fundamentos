package com.openlib.market.domain.api;

import java.time.LocalDateTime;
import java.util.UUID;

public class CredencialApi {
    private final String id;
    private final String idPropietario;
    private final String nombreApp;
    private final ApiKey llave;
    private EstadoLlave estado;
    private final LocalDateTime fechaCreacion;

    public CredencialApi(String idPropietario, String nombreApp) {
        if (idPropietario == null || idPropietario.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del propietario es obligatorio");
        }
        if (nombreApp == null || nombreApp.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la aplicación es obligatorio");
        }

        this.id = UUID.randomUUID().toString();
        this.idPropietario = idPropietario;
        this.nombreApp = nombreApp;
        this.llave = ApiKey.generarNueva();
        this.estado = EstadoLlave.ACTIVA;
        this.fechaCreacion = LocalDateTime.now();
    }

    public CredencialApi(String id, String idPropietario, String nombreApp, String llaveStr, EstadoLlave estado) {
        this.id = id;
        this.idPropietario = idPropietario;
        this.nombreApp = nombreApp;
        this.llave = new ApiKey(llaveStr);
        this.estado = estado;
        this.fechaCreacion = LocalDateTime.now(); // Simplificado para la reconstrucción
    }

    public String getId() { return id; }
    public String getIdPropietario() { return idPropietario; }
    public String getNombreApp() { return nombreApp; }
    public ApiKey getLlave() { return llave; }
    public EstadoLlave getEstado() { return estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public void revocar() {
        if (this.estado == EstadoLlave.REVOCADA) {
            throw new IllegalStateException("La llave ya está revocada");
        }
        this.estado = EstadoLlave.REVOCADA;
    }
}
