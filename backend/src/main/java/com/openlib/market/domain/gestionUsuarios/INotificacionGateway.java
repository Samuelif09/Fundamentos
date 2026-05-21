package com.openlib.market.domain.gestionUsuarios;

public interface INotificacionGateway {
    void notificarSuspension(String emailDestino, String motivo);
    void notificarAprobacion(String emailDestino);
}
