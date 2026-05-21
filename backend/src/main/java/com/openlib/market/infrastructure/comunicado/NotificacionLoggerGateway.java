package com.openlib.market.infrastructure.comunicado;

import com.openlib.market.domain.comunicado.ComunicadoMasivo;
import com.openlib.market.domain.comunicado.INotificacionGateway;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificacionLoggerGateway implements INotificacionGateway {

    @Override
    public void enviarComunicadoMasivo(ComunicadoMasivo comunicado, List<String> correosDestinatarios) {
        System.out.println("====== INICIANDO ENVÍO MASIVO ======");
        System.out.println("ID Comunicado: " + comunicado.getId());
        System.out.println("Asunto: " + comunicado.getAsunto());
        System.out.println("Destinatarios: " + correosDestinatarios.size() + " (" + comunicado.getFiltro() + ")");
        System.out.println("=====================================");
    }
}
