package com.openlib.market.application.anomalias;

import com.openlib.market.domain.anomalias.MetricaObjetivo;
import com.openlib.market.domain.anomalias.ReglaAnomalia;

import java.util.List;

public interface IMetricasGateway {
    List<ReglaAnomalia> obtenerReglasActivas();
    double obtenerValorActualMetrica(MetricaObjetivo metricaObjetivo);
}
