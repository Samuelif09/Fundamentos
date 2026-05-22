package com.openlib.market.infrastructure.monitoreo;

import com.openlib.market.application.anomalias.IEvaluarAnomaliaUseCase;
import com.openlib.market.domain.monitoreo.MetricaObjetivo;
import com.openlib.market.domain.monitoreo.ReglaAnomalia;
import com.openlib.market.domain.monitoreo.UmbralCritico;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;


public class AnomaliaScheduledTask {

    private final IEvaluarAnomaliaUseCase evaluarAnomaliaUseCase;
    private final List<ReglaAnomalia> reglasConfiguradas;

    public AnomaliaScheduledTask(IEvaluarAnomaliaUseCase evaluarAnomaliaUseCase) {
        this.evaluarAnomaliaUseCase = evaluarAnomaliaUseCase;
        
        // Simulamos reglas configuradas por el administrador en base de datos.
        // Alerta si los fallos de pago superan el 15%
        this.reglasConfiguradas = List.of(
            new ReglaAnomalia(MetricaObjetivo.FALLOS_PAGO, new UmbralCritico(15.0))
        );
    }

    // Se ejecuta cada 30 segundos para demostración (en producción podría ser cada 5 min).
    // Nota: Requiere @EnableScheduling en la clase principal (OpenLibApplication)
    @Scheduled(fixedRate = 30000)
    public void ejecutarMonitoreo() {
        System.out.println("🔄 [MONITOREO] Evaluando anomalías...");
        evaluarAnomaliaUseCase.evaluarAnomalias();
    }
}
