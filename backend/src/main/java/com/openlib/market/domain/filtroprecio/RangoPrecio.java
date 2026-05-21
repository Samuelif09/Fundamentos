package com.openlib.market.domain.filtroprecio;

public class RangoPrecio {
    private final double min;
    private final double max;

    public RangoPrecio(double min, double max) {
        if (min < 0) {
            throw new IllegalArgumentException("El precio mínimo no puede ser negativo");
        }
        if (max < min) {
            throw new IllegalArgumentException("El precio máximo no puede ser menor al mínimo");
        }
        this.min = min;
        this.max = max;
    }

    public double getMin() { return min; }
    public double getMax() { return max; }

    public boolean estaDentroDelRango(double precio) {
        return precio >= min && precio <= max;
    }
}
