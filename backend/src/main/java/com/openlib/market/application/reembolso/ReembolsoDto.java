package com.openlib.market.application.reembolso;

public class ReembolsoDto {
    private final String id;
    private final String idPedido;
    private final double montoReembolso;
    private final String motivo;
    private final String estado;

    public ReembolsoDto(String id, String idPedido, double montoReembolso, String motivo, String estado) {
        this.id = id;
        this.idPedido = idPedido;
        this.montoReembolso = montoReembolso;
        this.motivo = motivo;
        this.estado = estado;
    }

    public String getId() { return id; }
    public String getIdPedido() { return idPedido; }
    public double getMontoReembolso() { return montoReembolso; }
    public String getMotivo() { return motivo; }
    public String getEstado() { return estado; }
}
