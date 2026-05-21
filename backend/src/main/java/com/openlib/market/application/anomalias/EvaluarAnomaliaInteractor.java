package com.openlib.market.application.anomalias;

import com.openlib.market.domain.anomalias.Alerta;
import com.openlib.market.domain.anomalias.ReglaAnomalia;
import com.openlib.market.domain.anomalias.ReglaAnomaliaDomainService;

import java.util.List;
import java.util.Optional;

public class EvaluarAnomaliaInteractor implements IEvaluarAnomaliaUseCase {

    private final IMetricasGateway metricasGateway;
    private final INotificacionGateway notificacionGateway;
    private final ReglaAnomaliaDomainService domainService;

    public EvaluarAnomaliaInteractor(IMetricasGateway metricasGateway, INotificacionGateway notificacionGateway, ReglaAnomaliaDomainService domainService) {
        this.metricasGateway = metricasGateway;
        this.notificacionGateway = notificacionGateway;
        this.domainService = domainService;
    }

    @Override
    public void evaluarAnomalias() {
        List<ReglaAnomalia> reglas = metricasGateway.obtenerReglasActivas();

        for (ReglaAnomalia regla : reglas) {
            double valorActual = metricasGateway.obtenerValorActualMetrica(regla.getMetricaObjetivo());
            
            Optional<Alerta> alertaOpt = domainService.evaluarRegla(regla, valorActual);
            
            alertaOpt.ifPresent(notificacionGateway::enviarAlerta);
        }
    }
}
