package com.openlib.market.application.catalogo;

import java.util.List;

public class CatalogoPaginadoResponse {
    private final List<LibroCatalogoDto> libros;
    private final int paginaActual;
    private final int totalPaginas;
    private final long totalElementos;
    private final boolean tieneSiguiente;

    public CatalogoPaginadoResponse(List<LibroCatalogoDto> libros, int paginaActual, int totalPaginas, long totalElementos, boolean tieneSiguiente) {
        this.libros = libros;
        this.paginaActual = paginaActual;
        this.totalPaginas = totalPaginas;
        this.totalElementos = totalElementos;
        this.tieneSiguiente = tieneSiguiente;
    }

    public List<LibroCatalogoDto> getLibros() { return libros; }
    public int getPaginaActual() { return paginaActual; }
    public int getTotalPaginas() { return totalPaginas; }
    public long getTotalElementos() { return totalElementos; }
    public boolean getTieneSiguiente() { return tieneSiguiente; }
}
