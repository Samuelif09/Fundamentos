package com.openlib.market.infrastructure.adapter.in.web.dto;

public class AdminKpiDto {
    private int totalUsers;
    private int pendingSellers;
    private int totalBooks;
    private double platformRevenue;

    public AdminKpiDto() {}

    public AdminKpiDto(int totalUsers, int pendingSellers, int totalBooks, double platformRevenue) {
        this.totalUsers = totalUsers;
        this.pendingSellers = pendingSellers;
        this.totalBooks = totalBooks;
        this.platformRevenue = platformRevenue;
    }

    public int getTotalUsers() { return totalUsers; }
    public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }

    public int getPendingSellers() { return pendingSellers; }
    public void setPendingSellers(int pendingSellers) { this.pendingSellers = pendingSellers; }

    public int getTotalBooks() { return totalBooks; }
    public void setTotalBooks(int totalBooks) { this.totalBooks = totalBooks; }

    public double getPlatformRevenue() { return platformRevenue; }
    public void setPlatformRevenue(double platformRevenue) { this.platformRevenue = platformRevenue; }
}
