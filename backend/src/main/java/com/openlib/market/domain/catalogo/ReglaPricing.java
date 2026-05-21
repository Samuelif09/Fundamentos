package com.openlib.market.domain.catalogo;

import com.openlib.market.domain.detalle.Isbn;

public class ReglaPricing {
    private final Isbn idLibro;
    private final String idVendedor;
    private final PrecioMinimo precioMinimo;
    private final PrecioMaximo precioMaximo;
    private final EstrategiaCompetencia estrategia;

    public ReglaPricing(Isbn idLibro, String idVendedor, PrecioMinimo precioMinimo, PrecioMaximo precioMaximo, EstrategiaCompetencia estrategia) {
        if (idLibro == null) throw new IllegalArgumentException("ID del libro es requerido");
        if (idVendedor == null || idVendedor.isBlank()) throw new IllegalArgumentException("ID del vendedor es requerido");
        if (precioMinimo == null || precioMaximo == null) throw new IllegalArgumentException("Los límites de precio son requeridos");
        if (precioMinimo.getValor() > precioMaximo.getValor()) {
            throw new IllegalArgumentException("El precio mínimo no puede ser mayor al precio máximo");
        }
        if (estrategia == null) throw new IllegalArgumentException("La estrategia es requerida");

        this.idLibro = idLibro;
        this.idVendedor = idVendedor;
        this.precioMinimo = precioMinimo;
        this.precioMaximo = precioMaximo;
        this.estrategia = estrategia;
    }

    public Isbn getIdLibro() { return idLibro; }
    public String getIdVendedor() { return idVendedor; }
    public PrecioMinimo getPrecioMinimo() { return precioMinimo; }
    public PrecioMaximo getPrecioMaximo() { return precioMaximo; }
    public EstrategiaCompetencia getEstrategia() { return estrategia; }
}
