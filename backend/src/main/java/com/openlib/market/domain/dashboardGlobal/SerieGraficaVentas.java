package com.openlib.market.domain.dashboardGlobal;

import java.util.List;

public class SerieGraficaVentas {
    private final IntervaloTiempo intervalo;
    private final List<PuntoDatos> puntos;

    public SerieGraficaVentas(IntervaloTiempo intervalo, List<PuntoDatos> puntos) {
        if (intervalo == null) throw new IllegalArgumentException("Intervalo es requerido");
        if (puntos == null) throw new IllegalArgumentException("Los puntos no pueden ser nulos");
        this.intervalo = intervalo;
        this.puntos = puntos;
    }

    public IntervaloTiempo getIntervalo() { return intervalo; }
    public List<PuntoDatos> getPuntos() { return puntos; }
    
    public double getTotalAcumuladoSerie() {
        return puntos.stream().mapToDouble(PuntoDatos::getValorAcumulado).sum();
    }
}
