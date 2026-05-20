package com.openlib.market.domain.autenticacion;

public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException() {
        super("Email o contraseña incorrectos");
    }
}
