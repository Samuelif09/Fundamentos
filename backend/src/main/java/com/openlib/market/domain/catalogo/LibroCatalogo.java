package com.openlib.market.domain.catalogo;

public record LibroCatalogo(String isbn, String titulo, String autor, double precio, String urlPortada, int stock, String estado) {
    public LibroCatalogo(String isbn, String titulo, double precio, String urlPortada) {
        this(isbn, titulo, "Desconocido", precio, urlPortada, 10, "ACTIVO");
    }
    
    public LibroCatalogo(String isbn, String titulo, double precio, String urlPortada, int stock, String estado) {
        this(isbn, titulo, "Desconocido", precio, urlPortada, stock, estado);
    }

    public LibroCatalogo(String isbn, String titulo, String autor, double precio, String urlPortada) {
        this(isbn, titulo, autor, precio, urlPortada, 10, "ACTIVO");
    }
}
