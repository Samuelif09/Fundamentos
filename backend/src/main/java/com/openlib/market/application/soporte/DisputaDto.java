package com.openlib.market.application.soporte;

public class DisputaDto {
    private final String id;
    private final String idPedido;
    private final String estado;
    private final String resolucion;

    public DisputaDto(String id, String idPedido, String estado, String resolucion) {
        this.id = id;
        this.idPedido = idPedido;
        this.estado = estado;
        this.resolucion = resolucion;
    }

    public String getId() { return id; }
    public String getIdPedido() { return idPedido; }
    public String getEstado() { return estado; }
    public String getResolucion() { return resolucion; }
}
