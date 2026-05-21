package com.openlib.market.domain.monitoreo;

import java.util.UUID;

public class ReglaAnomalia {
    private final String id;
    private final MetricaObjetivo metrica;
    private final UmbralCritico umbral;
    private final boolean activa;

    public ReglaAnomalia(MetricaObjetivo metrica, UmbralCritico umbral) {
        this.id = UUID.randomUUID().toString();
        this.metrica = metrica;
        this.umbral = umbral;
        this.activa = true;
    }

    public ReglaAnomalia(String id, MetricaObjetivo metrica, UmbralCritico umbral, boolean activa) {
        this.id = id;
        this.metrica = metrica;
        this.umbral = umbral;
        this.activa = activa;
    }

    public String getId() { return id; }
    public MetricaObjetivo getMetrica() { return metrica; }
    public UmbralCritico getUmbral() { return umbral; }
    public boolean isActiva() { return activa; }
}
