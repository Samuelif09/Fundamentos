package com.openlib.market.domain.finanzas;
import java.math.BigDecimal;
public class VentaPlanaDto {
    private String tipoProducto;
    private BigDecimal precioUnitario;
    private int cantidad;
    private java.time.LocalDateTime fecha;

    public VentaPlanaDto(String tipoProducto, BigDecimal precioUnitario, int cantidad, java.time.LocalDateTime fecha) {
        this.tipoProducto = tipoProducto;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    public String getTipoProducto() { return tipoProducto; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public int getCantidad() { return cantidad; }
    public java.time.LocalDateTime getFecha() { return fecha; }
}
