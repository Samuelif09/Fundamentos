package com.openlib.market.domain.notificacionrebaja;

public class LibroCambioPrecioEvent {
    private final String idLibro;
    private final double precioAnterior;
    private final double precioNuevo;

    public LibroCambioPrecioEvent(String idLibro, double precioAnterior, double precioNuevo) {
        this.idLibro = idLibro;
        this.precioAnterior = precioAnterior;
        this.precioNuevo = precioNuevo;
    }

    public String getIdLibro() { return idLibro; }
    public double getPrecioAnterior() { return precioAnterior; }
    public double getPrecioNuevo() { return precioNuevo; }
}
