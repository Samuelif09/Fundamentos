package com.openlib.market.frontend.model;

public class WithdrawalRequest {
    private double monto;

    public WithdrawalRequest() {}
    public WithdrawalRequest(double monto) { this.monto = monto; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
}
