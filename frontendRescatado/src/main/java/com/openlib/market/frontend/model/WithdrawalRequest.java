package com.openlib.market.frontend.model;

public class WithdrawalRequest {
    private double monto;
    private String cuentaDestino;

    public WithdrawalRequest() {}

    public WithdrawalRequest(double monto, String cuentaDestino) {
        this.monto = monto;
        this.cuentaDestino = cuentaDestino;
    }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getCuentaDestino() { return cuentaDestino; }
    public void setCuentaDestino(String cuentaDestino) { this.cuentaDestino = cuentaDestino; }
}
