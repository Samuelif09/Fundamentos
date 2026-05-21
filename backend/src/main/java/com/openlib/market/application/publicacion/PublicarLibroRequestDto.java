package com.openlib.market.application.publicacion;

public class PublicarLibroRequestDto {
    private final String idVendedor;
    private final String isbn;
    private final String titulo;
    private final String sinopsis;
    private final double precio;
    private final String urlPortada;
    private final String categoria;

    public PublicarLibroRequestDto(String idVendedor, String isbn, String titulo, String sinopsis, double precio, String urlPortada, String categoria) {
        this.idVendedor = idVendedor;
        this.isbn = isbn;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.precio = precio;
        this.urlPortada = urlPortada;
        this.categoria = categoria;
    }

    public String getIdVendedor() { return idVendedor; }
    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public String getSinopsis() { return sinopsis; }
    public double getPrecio() { return precio; }
    public String getUrlPortada() { return urlPortada; }
    public String getCategoria() { return categoria; }
}
