package com.openlib.market.domain.shared;

public class AccionNoPermitidaException extends RuntimeException {
    public AccionNoPermitidaException(String message) {
        super(message);
    }
}
