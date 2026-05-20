package com.openlib.market.domain.pago;

public class PagoRechazadoException extends RuntimeException {
    public PagoRechazadoException(String mensaje) {
        super(mensaje);
    }
}
