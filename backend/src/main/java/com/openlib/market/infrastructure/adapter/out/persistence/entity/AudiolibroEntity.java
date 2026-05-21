package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "audiolibros")
public class AudiolibroEntity extends ContenidoDigitalEntity {

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    public AudiolibroEntity() {
        super();
    }

    public AudiolibroEntity(String isbn, String titulo, String sinopsis, double precio, String urlPortada, String categoria, String idVendedor, String estado, String urlVistaPrevia, int stockDisponible, Integer duracionMinutos) {
        super(isbn, titulo, sinopsis, precio, urlPortada, categoria, idVendedor, estado, urlVistaPrevia, stockDisponible);
        this.duracionMinutos = duracionMinutos;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }
}
