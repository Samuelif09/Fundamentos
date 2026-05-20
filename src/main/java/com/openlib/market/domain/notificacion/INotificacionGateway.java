package com.openlib.market.domain.notificacion;

public interface INotificacionGateway {
    void enviarReciboEmail(EmailDestino destino, ReciboCompra recibo);
    void notificarRechazoLibro(String idVendedor, String tituloLibro, String motivo);
}
