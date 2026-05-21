package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "disputas")
public class DisputaEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String idPedido;

    @Column(nullable = false)
    private String idComprador;

    @Column(nullable = false)
    private String idVendedor;

    @Column(nullable = false)
    private String motivo;

    @Column(nullable = false)
    private String estado; // ABIERTA | EN_MEDIACION | RESUELTA

    @Column(nullable = false)
    private String resolucion; // PENDIENTE | FAVOR_COMPRADOR | FAVOR_VENDEDOR

    public DisputaEntity() {}

    public DisputaEntity(String id, String idPedido, String idComprador, String idVendedor,
                         String motivo, String estado, String resolucion) {
        this.id = id;
        this.idPedido = idPedido;
        this.idComprador = idComprador;
        this.idVendedor = idVendedor;
        this.motivo = motivo;
        this.estado = estado;
        this.resolucion = resolucion;
    }

    public String getId() { return id; }
    public String getIdPedido() { return idPedido; }
    public String getIdComprador() { return idComprador; }
    public String getIdVendedor() { return idVendedor; }
    public String getMotivo() { return motivo; }
    public String getEstado() { return estado; }
    public String getResolucion() { return resolucion; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setResolucion(String resolucion) { this.resolucion = resolucion; }
}
