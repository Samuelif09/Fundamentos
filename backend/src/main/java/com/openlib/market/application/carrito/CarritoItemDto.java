package com.openlib.market.application.carrito;

public class CarritoItemDto {
    private String isbn;
    private String nombreProducto;
    private int cantidad;
    private double precioUnitario;

    public CarritoItemDto(String isbn, String nombreProducto, int cantidad, double precioUnitario) {
        this.isbn = isbn;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
}
