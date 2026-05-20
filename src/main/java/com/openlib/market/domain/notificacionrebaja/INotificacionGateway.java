package com.openlib.market.domain.notificacionrebaja;

public interface INotificacionGateway {
    void enviarAlertaPrecio(String idUsuario, String idLibro, double nuevoPrecio);
}
