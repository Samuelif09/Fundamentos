package com.openlib.market.domain.finanzas;

public class TransaccionLiquidacion {
    private final TransaccionFinanciera transaccionBase;
    private final DesgloseFinanciero desglose;

    public TransaccionLiquidacion(TransaccionFinanciera transaccionBase, DesgloseFinanciero desglose) {
        this.transaccionBase = transaccionBase;
        this.desglose = desglose;
    }

    public TransaccionFinanciera getTransaccionBase() { return transaccionBase; }
    public DesgloseFinanciero getDesglose() { return desglose; }
}
