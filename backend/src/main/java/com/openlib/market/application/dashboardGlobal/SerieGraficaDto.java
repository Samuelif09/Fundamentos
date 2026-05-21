package com.openlib.market.application.dashboardGlobal;

import java.util.List;
import java.util.Map;

public class SerieGraficaDto {
    private final String intervalo;
    private final List<Map<String, Object>> puntos;
    private final double totalGlobal;

    public SerieGraficaDto(String intervalo, List<Map<String, Object>> puntos, double totalGlobal) {
        this.intervalo = intervalo;
        this.puntos = puntos;
        this.totalGlobal = totalGlobal;
    }

    public String getIntervalo() { return intervalo; }
    public List<Map<String, Object>> getPuntos() { return puntos; }
    public double getTotalGlobal() { return totalGlobal; }
}
