package com.openlib.market.application.pago;

public class RealizarPagoRequestDto {
    private String sesionId;
    private double montoTotal;
    private String tipoPago;
    private String detallePago;

    public RealizarPagoRequestDto() {}

    public RealizarPagoRequestDto(String sesionId, double montoTotal, String tipoPago, String detallePago) {
        this.sesionId = sesionId;
        this.montoTotal = montoTotal;
        this.tipoPago = tipoPago;
        this.detallePago = detallePago;
    }

    public String getSesionId() { return sesionId; }
    public void setSesionId(String sesionId) { this.sesionId = sesionId; }

    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }

    public String getTipoPago() { return tipoPago; }
    public void setTipoPago(String tipoPago) { this.tipoPago = tipoPago; }

    public String getDetallePago() { return detallePago; }
    public void setDetallePago(String detallePago) { this.detallePago = detallePago; }
}
