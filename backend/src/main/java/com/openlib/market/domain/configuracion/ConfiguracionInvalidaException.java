package com.openlib.market.domain.configuracion;

public class ConfiguracionInvalidaException extends RuntimeException {
    public ConfiguracionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
