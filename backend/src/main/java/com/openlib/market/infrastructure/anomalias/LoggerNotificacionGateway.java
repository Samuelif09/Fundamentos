package com.openlib.market.infrastructure.anomalias;

import com.openlib.market.application.anomalias.INotificacionGateway;
import com.openlib.market.domain.anomalias.Alerta;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class LoggerNotificacionGateway implements INotificacionGateway {

    private static final Logger LOGGER = Logger.getLogger(LoggerNotificacionGateway.class.getName());

    @Override
    public void enviarAlerta(Alerta alerta) {
        // Entrega 1: Se imprimen por consola (Loggers/Webhooks)
        String mensaje = String.format(
            "🚨 [ALERTA CRÍTICA] Anomalía detectada en: %s | Valor registrado: %.2f | Umbral configurado: %.2f | ID: %s",
            alerta.getReglaInfringida().getMetricaObjetivo(),
            alerta.getValorRegistrado(),
            alerta.getReglaInfringida().getUmbral().valor(),
            alerta.getId()
        );
        LOGGER.warning(mensaje);
        
        // Aquí se implementaría la llamada real HTTP a un Webhook de Slack/Email en Entrega 2
    }
}
