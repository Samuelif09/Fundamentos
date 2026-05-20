package com.openlib.market.application.reembolso;

public interface IGestionarReembolsosUseCase {
    ReembolsoDto solicitarReembolso(String idPedido, double monto, String motivo);
    void aprobarReembolso(String idSolicitud);
    void denegarReembolso(String idSolicitud);
}
