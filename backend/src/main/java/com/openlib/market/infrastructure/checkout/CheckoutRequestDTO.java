package com.openlib.market.infrastructure.checkout;

public class CheckoutRequestDTO {
    private String idUsuario;
    private String metodoPago;

    public CheckoutRequestDTO() {}

    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
}
