package com.openlib.market.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SellerFinance {
    private double totalRevenue;
    private double pendingBalance;
    private int totalOrders;

    public SellerFinance() {}

    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }

    public double getPendingBalance() { return pendingBalance; }
    public void setPendingBalance(double pendingBalance) { this.pendingBalance = pendingBalance; }

    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }
}
