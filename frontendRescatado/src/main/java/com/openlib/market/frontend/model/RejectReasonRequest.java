package com.openlib.market.frontend.model;

public class RejectReasonRequest {
    private String motivo;

    public RejectReasonRequest() {}
    public RejectReasonRequest(String motivo) { this.motivo = motivo; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
