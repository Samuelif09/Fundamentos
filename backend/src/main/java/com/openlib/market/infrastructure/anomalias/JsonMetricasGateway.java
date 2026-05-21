package com.openlib.market.infrastructure.anomalias;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openlib.market.application.anomalias.IMetricasGateway;
import com.openlib.market.domain.anomalias.MetricaObjetivo;
import com.openlib.market.domain.anomalias.ReglaAnomalia;
import com.openlib.market.domain.anomalias.UmbralCritico;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class JsonMetricasGateway implements IMetricasGateway {

    private final ObjectMapper objectMapper;
    private final File jsonFile;

    public JsonMetricasGateway() {
        this.objectMapper = new ObjectMapper();
        this.jsonFile = new File("metricas.json");
    }

    @Override
    public List<ReglaAnomalia> obtenerReglasActivas() {
        // En un sistema real esto vendría de una BD relacional
        // Para Entrega 1: quemamos una regla por defecto o la leemos de conf
        return List.of(
                new ReglaAnomalia(MetricaObjetivo.FALLOS_PAGO, new UmbralCritico(15.0)),
                new ReglaAnomalia(MetricaObjetivo.TRAFICO, new UmbralCritico(10000.0))
        );
    }

    @Override
    public double obtenerValorActualMetrica(MetricaObjetivo metricaObjetivo) {
        if (!jsonFile.exists()) {
            return simularMetrica(metricaObjetivo);
        }

        try {
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            JsonNode metricaNode = rootNode.path(metricaObjetivo.name());
            if (!metricaNode.isMissingNode()) {
                return metricaNode.asDouble();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return simularMetrica(metricaObjetivo);
    }
    
    private double simularMetrica(MetricaObjetivo metricaObjetivo) {
        // Simulación para no fallar si no existe el archivo JSON
        if (metricaObjetivo == MetricaObjetivo.FALLOS_PAGO) {
            return 20.0; // Simulando 20% de fallos
        }
        return 0.0;
    }
}
