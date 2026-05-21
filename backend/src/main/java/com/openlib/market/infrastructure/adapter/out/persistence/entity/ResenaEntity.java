package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import java.time.LocalDate;

@Entity
@Table(name = "resenas")
public class ResenaEntity {

    @Id
    private String id;

    @Column(name = "isbn_libro", insertable = false, updatable = false)
    private String isbnLibro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "isbn_libro")
    private ContenidoDigitalEntity libro;

    private int calificacion;

    private String texto;

    private LocalDate fecha;

    public ResenaEntity() {}

    public ResenaEntity(String id, ContenidoDigitalEntity libro, int calificacion, String texto, LocalDate fecha) {
        this.id = id;
        this.libro = libro;
        this.calificacion = calificacion;
        this.texto = texto;
        this.fecha = fecha;
    }

    public String getId() { return id; }
    public String getIsbnLibro() { return isbnLibro; }
    public ContenidoDigitalEntity getLibro() { return libro; }
    public int getCalificacion() { return calificacion; }
    public String getTexto() { return texto; }
    public LocalDate getFecha() { return fecha; }
}
