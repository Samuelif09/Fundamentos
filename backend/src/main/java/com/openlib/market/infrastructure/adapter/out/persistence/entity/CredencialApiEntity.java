package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "credenciales_api")
public class CredencialApiEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String idPropietario;

    @Column(nullable = false)
    private String nombreApp;

    @Column(nullable = false, unique = true)
    private String valorLlave;

    @Column(nullable = false)
    private String estado; // ACTIVA | REVOCADA

    public CredencialApiEntity() {}

    public CredencialApiEntity(String id, String idPropietario, String nombreApp,
                               String valorLlave, String estado) {
        this.id = id;
        this.idPropietario = idPropietario;
        this.nombreApp = nombreApp;
        this.valorLlave = valorLlave;
        this.estado = estado;
    }

    public String getId() { return id; }
    public String getIdPropietario() { return idPropietario; }
    public String getNombreApp() { return nombreApp; }
    public String getValorLlave() { return valorLlave; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
