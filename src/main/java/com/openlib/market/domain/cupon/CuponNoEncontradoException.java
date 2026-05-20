package com.openlib.market.domain.cupon;

public class CuponNoEncontradoException extends RuntimeException {
    public CuponNoEncontradoException(String codigo) {
        super("No se encontró el cupón: " + codigo);
    }
}
