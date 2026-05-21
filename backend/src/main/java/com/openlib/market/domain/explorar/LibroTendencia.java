package com.openlib.market.domain.explorar;

import java.time.LocalDate;

public class LibroTendencia {
    private final String isbn;
    private final String titulo;
    private final int ventasTotales;
    private final double calificacion;
    private final LocalDate fechaPublicacion;

    public LibroTendencia(String isbn, String titulo, int ventasTotales, double calificacion, LocalDate fechaPublicacion) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.ventasTotales = ventasTotales;
        this.calificacion = calificacion;
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public int getVentasTotales() { return ventasTotales; }
    public double getCalificacion() { return calificacion; }
    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
}
