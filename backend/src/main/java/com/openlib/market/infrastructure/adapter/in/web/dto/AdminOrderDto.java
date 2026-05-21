package com.openlib.market.infrastructure.adapter.in.web.dto;

public class AdminOrderDto {
    private String orderId;
    private String buyerEmail;
    private double totalAmount;
    private String status;
    private String createdAt;

    public AdminOrderDto() {}

    public AdminOrderDto(String orderId, String buyerEmail, double totalAmount, String status, String createdAt) {
        this.orderId = orderId;
        this.buyerEmail = buyerEmail;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getBuyerEmail() { return buyerEmail; }
    public void setBuyerEmail(String buyerEmail) { this.buyerEmail = buyerEmail; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
