package com.openlib.market.infrastructure.monitoreo;

import com.openlib.market.domain.monitoreo.Alerta;
import com.openlib.market.domain.monitoreo.IAlertaNotificacionGateway;
import org.springframework.stereotype.Component;

@Component
public class WebhookAlertaGateway implements IAlertaNotificacionGateway {

    @Override
    public void enviarAlerta(Alerta alerta) {
        System.err.println("==========================================================");
        System.err.println("🚨 [WEBHOOK ALERTA DE ANOMALÍA] 🚨");
        System.err.println("ID Alerta: " + alerta.getId());
        System.err.println("Regla vulnerada: " + alerta.getIdRegla());
        System.err.println("Valor detectado: " + String.format("%.2f", alerta.getValorRegistrado()));
        System.err.println("Fecha/Hora: " + alerta.getFechaHora());
        System.err.println("==========================================================");
        // En un entorno real, aquí se ejecutaría un HTTP POST a Slack o PagerDuty
    }
}
