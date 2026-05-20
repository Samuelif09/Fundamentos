package com.openlib.market.domain.vendedor;

public class VendedorYaVerificadoException extends IllegalStateException {
    public VendedorYaVerificadoException() {
        super("El vendedor ya está verificado y aprobado.");
    }
}
