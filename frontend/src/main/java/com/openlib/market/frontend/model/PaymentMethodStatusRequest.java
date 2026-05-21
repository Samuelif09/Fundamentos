package com.openlib.market.frontend.model;

public class PaymentMethodStatusRequest {
    private String status;

    public PaymentMethodStatusRequest() {}
    public PaymentMethodStatusRequest(String status) { this.status = status; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
