package com.openlib.market.domain.pago;

public class MetodoPago {
    private final TipoMetodoPago tipo; 
    private final String detalle; // ej: "**** **** **** 1234"

    public MetodoPago(String tipoStr, String detalle) {
        if (tipoStr == null || tipoStr.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de método de pago es requerido");
        }
        if (detalle == null || detalle.trim().isEmpty()) {
            throw new IllegalArgumentException("El detalle del método de pago es requerido");
        }
        try {
            this.tipo = TipoMetodoPago.valueOf(tipoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de método de pago no válido: " + tipoStr);
        }
        this.detalle = detalle;
    }

    public TipoMetodoPago getTipo() { return tipo; }
    public String getDetalle() { return detalle; }
}
