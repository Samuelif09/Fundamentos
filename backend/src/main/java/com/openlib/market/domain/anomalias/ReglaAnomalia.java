package com.openlib.market.domain.anomalias;

import java.util.UUID;

public class ReglaAnomalia {
    private final String id;
    private final MetricaObjetivo metricaObjetivo;
    private final UmbralCritico umbral;

    public ReglaAnomalia(MetricaObjetivo metricaObjetivo, UmbralCritico umbral) {
        if (metricaObjetivo == null || umbral == null) {
            throw new IllegalArgumentException("Métrica y umbral son obligatorios");
        }
        this.id = UUID.randomUUID().toString();
        this.metricaObjetivo = metricaObjetivo;
        this.umbral = umbral;
    }

    public String getId() {
        return id;
    }

    public MetricaObjetivo getMetricaObjetivo() {
        return metricaObjetivo;
    }

    public UmbralCritico getUmbral() {
        return umbral;
    }

    public boolean evaluar(double valorActual) {
        return valorActual > umbral.valor();
    }
}
