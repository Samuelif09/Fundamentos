package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "reglas_comision")
public class ReglaComisionEntity {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String idCategoria; // "GLOBAL" para la regla por defecto

    @Column(nullable = false)
    private double porcentajeComision;

    public ReglaComisionEntity() {}

    public ReglaComisionEntity(String id, String idCategoria, double porcentajeComision) {
        this.id = id;
        this.idCategoria = idCategoria.toUpperCase();
        this.porcentajeComision = porcentajeComision;
    }

    public String getId() { return id; }
    public String getIdCategoria() { return idCategoria; }
    public double getPorcentajeComision() { return porcentajeComision; }
    public void setPorcentajeComision(double porcentajeComision) { this.porcentajeComision = porcentajeComision; }
}
