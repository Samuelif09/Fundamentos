package com.openlib.market.domain.vendedor;

public class VerificacionEnCursoException extends IllegalStateException {
    public VerificacionEnCursoException() {
        super("Ya existe una verificación en curso para este vendedor.");
    }
}
