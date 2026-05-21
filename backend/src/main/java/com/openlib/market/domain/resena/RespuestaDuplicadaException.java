package com.openlib.market.domain.resena;

public class RespuestaDuplicadaException extends IllegalStateException {
    public RespuestaDuplicadaException() {
        super("Esta reseña ya tiene una respuesta del vendedor");
    }
}
