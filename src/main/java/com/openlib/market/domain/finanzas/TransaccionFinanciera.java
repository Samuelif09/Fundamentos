package com.openlib.market.domain.finanzas;

public class TransaccionFinanciera {
    private final String idTransaccion;
    private final double subtotal;
    private final java.time.LocalDate fecha;

    public TransaccionFinanciera(String idTransaccion, double subtotal, java.time.LocalDate fecha) {
        this.idTransaccion = idTransaccion;
        this.subtotal = subtotal;
        this.fecha = fecha;
    }

    public String getIdTransaccion() { return idTransaccion; }
    public double getSubtotal() { return subtotal; }
    public java.time.LocalDate getFecha() { return fecha; }
}
