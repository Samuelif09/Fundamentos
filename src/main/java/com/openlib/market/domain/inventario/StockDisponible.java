package com.openlib.market.domain.inventario;

public class StockDisponible {
    private final int cantidad;

    public StockDisponible(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.cantidad = cantidad;
    }

    public int getCantidad() { return cantidad; }

    public boolean isDisponible() {
        return cantidad > 0;
    }
}
