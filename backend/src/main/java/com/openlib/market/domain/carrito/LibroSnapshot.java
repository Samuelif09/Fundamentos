package com.openlib.market.domain.carrito;

public class LibroSnapshot {
    private final String isbn;
    private final double precio;

    public LibroSnapshot(String isbn, double precio) {
        this.isbn = isbn;
        this.precio = precio;
    }

    public String getIsbn() { return isbn; }
    public double getPrecio() { return precio; }
}
