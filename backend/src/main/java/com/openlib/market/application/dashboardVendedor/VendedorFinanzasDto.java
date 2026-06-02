package com.openlib.market.application.dashboardVendedor;

public class VendedorFinanzasDto {
    private double totalRevenue;
    private double pendingBalance;
    private int totalOrders;

    public VendedorFinanzasDto(double totalRevenue, double pendingBalance, int totalOrders) {
        this.totalRevenue = totalRevenue;
        this.pendingBalance = pendingBalance;
        this.totalOrders = totalOrders;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public double getPendingBalance() {
        return pendingBalance;
    }

    public int getTotalOrders() {
        return totalOrders;
    }
}
