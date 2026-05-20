package com.openlib.market.domain.carrito;

public class ItemCarrito {
    private final String libroIsbn;
    private Cantidad cantidad;
    private final double precioUnitario;

    public ItemCarrito(String libroIsbn, Cantidad cantidad, double precioUnitario) {
        this.libroIsbn = libroIsbn;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public String getLibroIsbn() {
        return libroIsbn;
    }

    public Cantidad getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getSubtotal() {
        return precioUnitario * cantidad.getValor();
    }

    public void agregarCantidad(Cantidad cantidadExtra) {
        this.cantidad = this.cantidad.sumar(cantidadExtra);
    }
}
