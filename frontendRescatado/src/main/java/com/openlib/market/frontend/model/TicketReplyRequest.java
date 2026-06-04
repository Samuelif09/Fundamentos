package com.openlib.market.frontend.model;

public class TicketReplyRequest {
    private String mensaje;

    public TicketReplyRequest() {}
    public TicketReplyRequest(String mensaje) { this.mensaje = mensaje; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
