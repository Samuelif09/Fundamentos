package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "solicitudes_reembolso")
public class SolicitudReembolsoEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String idPedido;

    @Column(nullable = false)
    private double montoReembolso;

    @Column(nullable = false)
    private String motivo;

    @Column(nullable = false)
    private String estado;

    public SolicitudReembolsoEntity() {}

    public SolicitudReembolsoEntity(String id, String idPedido, double montoReembolso, String motivo, String estado) {
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
    public void setEstado(String estado) { this.estado = estado; }
}
