package com.openlib.market.application.pago;

public class CheckoutRequestDto {
    private String idUsuario;
    private String idPedido;
    private double montoTotal;
    private String tokenPago;

    public CheckoutRequestDto() {}

    public CheckoutRequestDto(String idUsuario, String idPedido, double montoTotal, String tokenPago) {
        this.idUsuario = idUsuario;
        this.idPedido = idPedido;
        this.montoTotal = montoTotal;
        this.tokenPago = tokenPago;
    }

    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public String getIdPedido() { return idPedido; }
    public void setIdPedido(String idPedido) { this.idPedido = idPedido; }

    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }

    public String getTokenPago() { return tokenPago; }
    public void setTokenPago(String tokenPago) { this.tokenPago = tokenPago; }
}
