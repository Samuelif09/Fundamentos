package com.openlib.market.frontend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminKpi {
    private int totalUsers;
    private int pendingSellers;
    private int totalBooks;
    private double platformRevenue;

    public AdminKpi() {}

    public int getTotalUsers() { return totalUsers; }
    public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }

    public int getPendingSellers() { return pendingSellers; }
    public void setPendingSellers(int pendingSellers) { this.pendingSellers = pendingSellers; }

    public int getTotalBooks() { return totalBooks; }
    public void setTotalBooks(int totalBooks) { this.totalBooks = totalBooks; }

    public double getPlatformRevenue() { return platformRevenue; }
    public void setPlatformRevenue(double platformRevenue) { this.platformRevenue = platformRevenue; }
}
