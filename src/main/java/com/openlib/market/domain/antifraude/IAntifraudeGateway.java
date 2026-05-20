package com.openlib.market.domain.antifraude;

public interface IAntifraudeGateway {
    EvaluacionFraude evaluarTransaccion(String idPedido, double monto);
}
