package com.openlib.market.infrastructure.adapter.in.web.dto;

public class TicketReplyRequestDto {
    private String message;

    public TicketReplyRequestDto() {}

    public TicketReplyRequestDto(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
