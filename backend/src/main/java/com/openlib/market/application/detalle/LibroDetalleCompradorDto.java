package com.openlib.market.application.detalle;

public class LibroDetalleCompradorDto {
    private final String isbn;
    private final String titulo;
    private final String sinopsis;
    private final double precio;
    private final boolean disponibleParaCompra;
    private final String autor;
    private final String urlPortada;

    public LibroDetalleCompradorDto(String isbn, String titulo, String sinopsis, double precio, boolean disponibleParaCompra, String autor, String urlPortada) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.precio = precio;
        this.disponibleParaCompra = disponibleParaCompra;
        this.autor = autor;
        this.urlPortada = urlPortada;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public String getSinopsis() { return sinopsis; }
    public double getPrecio() { return precio; }
    public boolean isDisponibleParaCompra() { return disponibleParaCompra; }
    public String getAutor() { return autor; }
    public String getUrlPortada() { return urlPortada; }
}
