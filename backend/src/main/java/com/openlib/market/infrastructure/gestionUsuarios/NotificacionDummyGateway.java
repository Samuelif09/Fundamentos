package com.openlib.market.infrastructure.gestionUsuarios;

import com.openlib.market.domain.gestionUsuarios.INotificacionGateway;
import org.springframework.stereotype.Component;

@Component
public class NotificacionDummyGateway implements INotificacionGateway {

    @Override
    public void notificarSuspension(String emailDestino, String motivo) {
        System.out.println("Enviando EMAIL de suspensión a " + emailDestino + " por el motivo: " + motivo);
    }

    @Override
    public void notificarAprobacion(String emailDestino) {
        System.out.println("Enviando EMAIL de aprobación a " + emailDestino);
    }
}
