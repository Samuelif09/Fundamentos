package com.openlib.market.application.detalle;

public class LibroDetalleCompradorDto {
    private final String isbn;
    private final String titulo;
    private final String sinopsis;
    private final double precio;
    private final boolean disponibleParaCompra;

    public LibroDetalleCompradorDto(String isbn, String titulo, String sinopsis, double precio, boolean disponibleParaCompra) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.precio = precio;
        this.disponibleParaCompra = disponibleParaCompra;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public String getSinopsis() { return sinopsis; }
    public double getPrecio() { return precio; }
    public boolean isDisponibleParaCompra() { return disponibleParaCompra; }
}
