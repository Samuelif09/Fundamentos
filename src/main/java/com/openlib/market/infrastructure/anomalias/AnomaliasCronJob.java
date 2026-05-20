package com.openlib.market.infrastructure.anomalias;

import com.openlib.market.application.anomalias.IEvaluarAnomaliaUseCase;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AnomaliasCronJob {

    private final IEvaluarAnomaliaUseCase evaluarAnomaliaUseCase;

    public AnomaliasCronJob(IEvaluarAnomaliaUseCase evaluarAnomaliaUseCase) {
        this.evaluarAnomaliaUseCase = evaluarAnomaliaUseCase;
    }

    // Se ejecuta cada 5 minutos
    @Scheduled(fixedRate = 300000)
    public void ejecutarMonitoreo() {
        evaluarAnomaliaUseCase.evaluarAnomalias();
    }
}
