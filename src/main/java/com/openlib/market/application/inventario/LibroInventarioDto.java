package com.openlib.market.application.inventario;

public class LibroInventarioDto {
    private final String isbn;
    private final String titulo;
    private final double precio;
    private final String categoria;
    private final String idVendedor;

    public LibroInventarioDto(String isbn, String titulo, double precio, String categoria, String idVendedor) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.precio = precio;
        this.categoria = categoria;
        this.idVendedor = idVendedor;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public double getPrecio() { return precio; }
    public String getCategoria() { return categoria; }
    public String getIdVendedor() { return idVendedor; }
}
