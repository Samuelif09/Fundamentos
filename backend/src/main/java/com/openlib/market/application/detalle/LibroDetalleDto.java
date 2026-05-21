package com.openlib.market.application.detalle;

public class LibroDetalleDto {
    private final String isbn;
    private final String titulo;
    private final String sinopsis;
    private final double precio;
    private final String urlPortada;

    public LibroDetalleDto(String isbn, String titulo, String sinopsis, double precio, String urlPortada) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.precio = precio;
        this.urlPortada = urlPortada;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public String getSinopsis() { return sinopsis; }
    public double getPrecio() { return precio; }
    public String getUrlPortada() { return urlPortada; }
}
