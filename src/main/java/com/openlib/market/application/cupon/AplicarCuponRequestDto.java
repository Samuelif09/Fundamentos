package com.openlib.market.application.cupon;

public class AplicarCuponRequestDto {
    private final String userId;
    private final String codigoCupon;

    public AplicarCuponRequestDto(String userId, String codigoCupon) {
        this.userId = userId;
        this.codigoCupon = codigoCupon;
    }

    public String getUserId() { return userId; }
    public String getCodigoCupon() { return codigoCupon; }
}
