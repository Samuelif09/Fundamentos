package com.openlib.market.domain.dashboardGlobal;

public class PuntoDatos {
    private final String etiquetaTemporal;
    private final double valorAcumulado;

    public PuntoDatos(String etiquetaTemporal, double valorAcumulado) {
        this.etiquetaTemporal = etiquetaTemporal;
        this.valorAcumulado = valorAcumulado;
    }

    public String getEtiquetaTemporal() { return etiquetaTemporal; }
    public double getValorAcumulado() { return valorAcumulado; }
}
