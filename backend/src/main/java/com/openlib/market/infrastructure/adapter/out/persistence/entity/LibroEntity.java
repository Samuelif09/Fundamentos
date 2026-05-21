package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "libros")
public class LibroEntity extends ContenidoDigitalEntity {

    public LibroEntity() {
        super();
    }

    public LibroEntity(String isbn, String titulo, String sinopsis, double precio, String urlPortada, String categoria, String idVendedor, String estado, String urlVistaPrevia, int stockDisponible) {
        super(isbn, titulo, sinopsis, precio, urlPortada, categoria, idVendedor, estado, urlVistaPrevia, stockDisponible);
    }
}
