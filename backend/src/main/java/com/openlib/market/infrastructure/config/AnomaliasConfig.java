package com.openlib.market.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.openlib.market.application.anomalias.IEvaluarAnomaliaUseCase;
import com.openlib.market.application.anomalias.EvaluarAnomaliaInteractor;
import com.openlib.market.application.anomalias.IMetricasGateway;
import com.openlib.market.application.anomalias.INotificacionGateway;
import com.openlib.market.domain.anomalias.ReglaAnomaliaDomainService;

@Configuration
public class AnomaliasConfig {

    // Registra primero el Domain Service si no tiene anotaciones de Spring en su clase original
    @Bean
    public ReglaAnomaliaDomainService reglaAnomaliaDomainService() {
        return new ReglaAnomaliaDomainService();
    }

    // Registra el Caso de Uso inyectando el Domain Service
    @Bean
    public IEvaluarAnomaliaUseCase evaluarAnomaliaUseCase(
            IMetricasGateway metricasGateway,
            INotificacionGateway notificacionGateway,
            ReglaAnomaliaDomainService domainService) {

        return new EvaluarAnomaliaInteractor(metricasGateway, notificacionGateway, domainService);
    }
}