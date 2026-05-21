package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "cursos_virtuales")
public class CursoVirtualEntity extends ContenidoDigitalEntity {

    @Column(name = "duracion_estimada_minutos")
    private Integer duracionEstimadaMinutos;

    public CursoVirtualEntity() {
        super();
    }

    public CursoVirtualEntity(String isbn, String titulo, String sinopsis, double precio, String urlPortada, String categoria, String idVendedor, String estado, String urlVistaPrevia, int stockDisponible, Integer duracionEstimadaMinutos) {
        super(isbn, titulo, sinopsis, precio, urlPortada, categoria, idVendedor, estado, urlVistaPrevia, stockDisponible);
        this.duracionEstimadaMinutos = duracionEstimadaMinutos;
    }

    public Integer getDuracionEstimadaMinutos() {
        return duracionEstimadaMinutos;
    }

    public void setDuracionEstimadaMinutos(Integer duracionEstimadaMinutos) {
        this.duracionEstimadaMinutos = duracionEstimadaMinutos;
    }
}
