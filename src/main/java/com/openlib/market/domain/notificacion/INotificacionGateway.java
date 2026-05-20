package com.openlib.market.domain.notificacion;

public interface INotificacionGateway {
    void enviarReciboEmail(EmailDestino destino, ReciboCompra recibo);
}
