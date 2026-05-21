package com.openlib.market.infrastructure.adapter.in.web.dto;

public class CheckoutResponseDto {
    private String status;
    private String orderId;
    private String message;

    public CheckoutResponseDto() {}

    public CheckoutResponseDto(String status, String orderId, String message) {
        this.status = status;
        this.orderId = orderId;
        this.message = message;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
