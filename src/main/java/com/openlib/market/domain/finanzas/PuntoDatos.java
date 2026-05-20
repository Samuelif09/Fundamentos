package com.openlib.market.domain.finanzas;

public class PuntoDatos {
    private final String fecha;
    private final double valor;

    public PuntoDatos(String fecha, double valor) {
        this.fecha = fecha;
        this.valor = valor;
    }

    public String getFecha() { return fecha; }
    public double getValor() { return valor; }
}
