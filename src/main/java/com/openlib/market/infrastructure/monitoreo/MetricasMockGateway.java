package com.openlib.market.infrastructure.monitoreo;

import com.openlib.market.domain.monitoreo.IMetricasGateway;
import com.openlib.market.domain.monitoreo.MetricaObjetivo;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MetricasMockGateway implements IMetricasGateway {

    private final Random random = new Random();

    @Override
    public double obtenerValorActual(MetricaObjetivo metrica) {
        // Simulamos métricas en tiempo real. 
        // Para FALLOS_PAGO generamos un valor entre 5 y 25.
        // (Si el umbral es 15, a veces disparará alerta, a veces no).
        if (metrica == MetricaObjetivo.FALLOS_PAGO) {
            return 5.0 + (random.nextDouble() * 20.0);
        }
        
        return random.nextDouble() * 100.0;
    }
}
