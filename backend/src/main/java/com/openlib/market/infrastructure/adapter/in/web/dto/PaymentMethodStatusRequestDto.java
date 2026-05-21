package com.openlib.market.infrastructure.adapter.in.web.dto;

public class PaymentMethodStatusRequestDto {
    private String status;

    public PaymentMethodStatusRequestDto() {}

    public PaymentMethodStatusRequestDto(String status) {
        this.status = status;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
