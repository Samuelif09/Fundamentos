package com.openlib.market.domain.catalogo;

import java.util.List;

public class PaginaDominio<T> {
    private final List<T> contenido;
    private final int paginaActual;
    private final int tamanoPagina;
    private final long totalElementos;
    private final int totalPaginas;

    public PaginaDominio(List<T> contenido, int paginaActual, int tamanoPagina, long totalElementos) {
        this.contenido = contenido;
        this.paginaActual = paginaActual;
        this.tamanoPagina = tamanoPagina;
        this.totalElementos = totalElementos;
        this.totalPaginas = (int) Math.ceil((double) totalElementos / tamanoPagina);
    }

    public List<T> getContenido() { return contenido; }
    public int getPaginaActual() { return paginaActual; }
    public int getTamanoPagina() { return tamanoPagina; }
    public long getTotalElementos() { return totalElementos; }
    public int getTotalPaginas() { return totalPaginas; }
    public boolean hasNext() { return paginaActual < totalPaginas - 1; }
    public boolean hasPrevious() { return paginaActual > 0; }
}
