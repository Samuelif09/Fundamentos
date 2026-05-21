package com.openlib.market.domain.reembolso;

public class MontoReembolsoInvalidoException extends RuntimeException {
    public MontoReembolsoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
