package com.openlib.market.application.busqueda;

public class LibroBuscadoDto {
    private final String id;
    private final String titulo;
    private final String autor;

    public LibroBuscadoDto(String id, String titulo, String autor) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
}
