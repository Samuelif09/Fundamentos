package com.openlib.market.application.monitoreo;

import com.openlib.market.domain.monitoreo.*;

import java.util.List;

public class EvaluarAnomaliaInteractor implements IEvaluarAnomaliaUseCase {

    private final IMetricasGateway metricasGateway;
    private final IAlertaNotificacionGateway notificacionGateway;
    private final ReglaAnomaliaDomainService domainService;

    public EvaluarAnomaliaInteractor(
            IMetricasGateway metricasGateway,
            IAlertaNotificacionGateway notificacionGateway,
            ReglaAnomaliaDomainService domainService) {
        this.metricasGateway = metricasGateway;
        this.notificacionGateway = notificacionGateway;
        this.domainService = domainService;
    }

    @Override
    public void evaluarReglas(List<ReglaAnomalia> reglas) {
        for (ReglaAnomalia regla : reglas) {
            double valorActual = metricasGateway.obtenerValorActual(regla.getMetrica());
            
            domainService.evaluar(regla, valorActual).ifPresent(alerta -> {
                notificacionGateway.enviarAlerta(alerta);
            });
        }
    }
}
