package com.openlib.market.infrastructure.notificacionrebaja;

import com.openlib.market.domain.notificacionrebaja.INotificacionGateway;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class NotificacionLoggerGateway implements INotificacionGateway {

    private static final Logger LOGGER = Logger.getLogger(NotificacionLoggerGateway.class.getName());

    @Override
    public void enviarAlertaPrecio(String idUsuario, String idLibro, double nuevoPrecio) {
        LOGGER.info(String.format("🔔 [ALERTA PRECIO] Enviando notificación al usuario '%s': El libro '%s' ha bajado de precio a $%.2f",
                idUsuario, idLibro, nuevoPrecio));
    }
}
