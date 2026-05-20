package com.openlib.market.domain.finanzas;

public class FondosInsuficientesException extends IllegalStateException {
    public FondosInsuficientesException(String message) {
        super(message);
    }
}
