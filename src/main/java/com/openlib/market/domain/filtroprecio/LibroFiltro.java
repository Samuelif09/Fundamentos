package com.openlib.market.domain.filtroprecio;

public class LibroFiltro {
    private final String isbn;
    private final String titulo;
    private final double precio;

    public LibroFiltro(String isbn, String titulo, double precio) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.precio = precio;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public double getPrecio() { return precio; }
}
