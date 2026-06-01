package com.openlib.market.domain.catalogo;

public record LibroCatalogo(String isbn, String titulo, double precio, String urlPortada, int stock, String estado) {
    public LibroCatalogo(String isbn, String titulo, double precio, String urlPortada) {
        this(isbn, titulo, precio, urlPortada, 10, "ACTIVO");
    }
}
