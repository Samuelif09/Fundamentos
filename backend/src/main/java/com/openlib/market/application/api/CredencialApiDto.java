package com.openlib.market.application.api;

public class CredencialApiDto {
    private final String id;
    private final String idPropietario;
    private final String nombreApp;
    private final String llave;
    private final String estado;

    public CredencialApiDto(String id, String idPropietario, String nombreApp, String llave, String estado) {
        this.id = id;
        this.idPropietario = idPropietario;
        this.nombreApp = nombreApp;
        this.llave = llave;
        this.estado = estado;
    }

    public String getId() { return id; }
    public String getIdPropietario() { return idPropietario; }
    public String getNombreApp() { return nombreApp; }
    public String getLlave() { return llave; }
    public String getEstado() { return estado; }
}
