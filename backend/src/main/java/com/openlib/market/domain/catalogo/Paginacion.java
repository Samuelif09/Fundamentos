package com.openlib.market.domain.catalogo;

public class Paginacion {
    private final int paginaActual;
    private final int tamanoPagina;

    public Paginacion(int paginaActual, int tamanoPagina) {
        if (paginaActual < 0) {
            throw new IllegalArgumentException("La página actual no puede ser negativa");
        }
        if (tamanoPagina <= 0) {
            throw new IllegalArgumentException("El tamaño de página debe ser mayor a cero");
        }
        if (tamanoPagina > 100) {
            throw new IllegalArgumentException("El tamaño de página no puede exceder 100 elementos");
        }
        this.paginaActual = paginaActual;
        this.tamanoPagina = tamanoPagina;
    }

    public int getPaginaActual() { return paginaActual; }
    public int getTamanoPagina() { return tamanoPagina; }
}
