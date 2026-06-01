package com.openlib.market.application.inventario;

public class LibroInventarioDto {
    private final String isbn;
    private final String titulo;
    private final double precio;
    private final String categoria;
    private final String idVendedor;
    private final int stock;
    private final String estado;

    public LibroInventarioDto(String isbn, String titulo, double precio, String categoria, String idVendedor, int stock, String estado) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.precio = precio;
        this.categoria = categoria;
        this.idVendedor = idVendedor;
        this.stock = stock;
        this.estado = estado;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public double getPrecio() { return precio; }
    public String getCategoria() { return categoria; }
    public String getIdVendedor() { return idVendedor; }
    public int getStock() { return stock; }
    public String getEstado() { return estado; }
}
