package com.openlib.market.application.filtroprecio;

public class LibroBuscadoDto {
    private final String isbn;
    private final String titulo;
    private final double precio;

    public LibroBuscadoDto(String isbn, String titulo, double precio) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.precio = precio;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public double getPrecio() { return precio; }
}
