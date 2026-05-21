package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "banners_promocionales")
public class BannerPromocionalEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String urlImagen;

    private String urlDestino;

    @Column(nullable = false)
    private LocalDateTime fechaInicio;

    @Column(nullable = false)
    private LocalDateTime fechaFin;

    @Column(nullable = false)
    private String estado; // ACTIVA | INACTIVA

    public BannerPromocionalEntity() {}

    public BannerPromocionalEntity(String id, String titulo, String urlImagen, String urlDestino,
                                   LocalDateTime fechaInicio, LocalDateTime fechaFin, String estado) {
        this.id = id;
        this.titulo = titulo;
        this.urlImagen = urlImagen;
        this.urlDestino = urlDestino;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getUrlImagen() { return urlImagen; }
    public String getUrlDestino() { return urlDestino; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
