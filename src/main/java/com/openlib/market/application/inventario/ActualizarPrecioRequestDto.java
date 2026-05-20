package com.openlib.market.application.inventario;

public class ActualizarPrecioRequestDto {
    private final String idVendedor;
    private final String isbn;
    private final double nuevoPrecio;

    public ActualizarPrecioRequestDto(String idVendedor, String isbn, double nuevoPrecio) {
        this.idVendedor = idVendedor;
        this.isbn = isbn;
        this.nuevoPrecio = nuevoPrecio;
    }

    public String getIdVendedor() { return idVendedor; }
    public String getIsbn() { return isbn; }
    public double getNuevoPrecio() { return nuevoPrecio; }
}
