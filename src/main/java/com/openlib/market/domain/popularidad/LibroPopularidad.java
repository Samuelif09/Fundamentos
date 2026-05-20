package com.openlib.market.domain.popularidad;

public class LibroPopularidad {
    private final String isbn;
    private final String titulo;
    private final int ventasUltimoMes;

    public LibroPopularidad(String isbn, String titulo, int ventasUltimoMes) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.ventasUltimoMes = ventasUltimoMes;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public int getVentasUltimoMes() { return ventasUltimoMes; }
}
