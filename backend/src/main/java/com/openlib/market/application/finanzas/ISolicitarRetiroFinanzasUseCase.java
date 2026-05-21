package com.openlib.market.application.finanzas;

public interface ISolicitarRetiroFinanzasUseCase {
    void solicitarRetiro(String idVendedor, double monto, String cuentaDestino);
}
