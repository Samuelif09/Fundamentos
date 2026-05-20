package com.openlib.market.application.explorar;

public class LibroTendenciaDto {
    private final String isbn;
    private final String titulo;

    public LibroTendenciaDto(String isbn, String titulo) {
        this.isbn = isbn;
        this.titulo = titulo;
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
}
