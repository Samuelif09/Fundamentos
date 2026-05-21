package com.openlib.market.application.catalogo;

public class LibroCatalogoDto {
    private final String isbn;
    private final String titulo;
    private final double precio;
    private final String urlPortada;

    public LibroCatalogoDto(String isbn, String titulo, double precio, String urlPortada) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.precio = precio;
        this.urlPortada = urlPortada;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public double getPrecio() { return precio; }
    public String getUrlPortada() { return urlPortada; }
}
