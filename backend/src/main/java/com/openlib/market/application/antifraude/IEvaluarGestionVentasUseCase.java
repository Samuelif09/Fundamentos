package com.openlib.market.application.antifraude;

public interface IEvaluarGestionVentasUseCase {
    boolean evaluarTransaccion(String idPedido, double monto);
}
