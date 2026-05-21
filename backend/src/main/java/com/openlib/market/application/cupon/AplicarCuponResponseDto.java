package com.openlib.market.application.cupon;

public class AplicarCuponResponseDto {
    private final double totalOriginal;
    private final double totalConDescuento;
    private final String codigoCupon;

    public AplicarCuponResponseDto(double totalOriginal, double totalConDescuento, String codigoCupon) {
        this.totalOriginal = totalOriginal;
        this.totalConDescuento = totalConDescuento;
        this.codigoCupon = codigoCupon;
    }

    public double getTotalOriginal() { return totalOriginal; }
    public double getTotalConDescuento() { return totalConDescuento; }
    public String getCodigoCupon() { return codigoCupon; }
}
