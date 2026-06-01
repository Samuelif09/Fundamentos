package com.openlib.market.application.carrito;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartBookDto {
    private final String isbn;
    private final String titulo;
    private final String autor;
    private final double precio;
    private final String urlPortada;
    private final String sinopsis;

    public CartBookDto(String isbn, String titulo, String autor, double precio, String urlPortada, String sinopsis) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.precio = precio;
        this.urlPortada = urlPortada;
        this.sinopsis = sinopsis;
    }

    @JsonProperty("isbn")
    public String getIsbn() { return isbn; }

    @JsonProperty("titulo")
    public String getTitulo() { return titulo; }

    @JsonProperty("autor")
    public String getAutor() { return autor; }

    @JsonProperty("precio")
    public double getPrecio() { return precio; }

    @JsonProperty("urlPortada")
    public String getUrlPortada() { return urlPortada; }

    @JsonProperty("sinopsis")
    public String getSinopsis() { return sinopsis; }
}
