package com.openlib.market.domain.finanzas;

public class BilleteraVendedor {
    private final String idVendedor;
    private double saldoDisponible;

    public BilleteraVendedor(String idVendedor, double saldoDisponible) {
        this.idVendedor = idVendedor;
        this.saldoDisponible = saldoDisponible;
    }

    public String getIdVendedor() { return idVendedor; }
    public double getSaldoDisponible() { return saldoDisponible; }

    public void retirar(MontoRetiro monto) {
        if (monto.getValor() > this.saldoDisponible) {
            throw new FondosInsuficientesException("Fondos insuficientes para realizar el retiro");
        }
        this.saldoDisponible -= monto.getValor();
    }
    
    public void depositar(double monto) {
        if (monto > 0) {
            this.saldoDisponible += monto;
        }
    }
}
