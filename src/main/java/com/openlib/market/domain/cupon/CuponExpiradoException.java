package com.openlib.market.domain.cupon;

public class CuponExpiradoException extends RuntimeException {
    public CuponExpiradoException(String codigo) {
        super("El cupón '" + codigo + "' ha expirado");
    }
}
