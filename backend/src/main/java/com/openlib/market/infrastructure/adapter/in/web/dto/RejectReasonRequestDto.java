package com.openlib.market.infrastructure.adapter.in.web.dto;

public class RejectReasonRequestDto {
    private String reason;

    public RejectReasonRequestDto() {}

    public RejectReasonRequestDto(String reason) {
        this.reason = reason;
    }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
