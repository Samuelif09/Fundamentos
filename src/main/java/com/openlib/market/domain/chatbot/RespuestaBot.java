package com.openlib.market.domain.chatbot;

public record RespuestaBot(String texto, double nivelConfianza) {
    public RespuestaBot {
        if (nivelConfianza < 0.0 || nivelConfianza > 1.0) {
            throw new IllegalArgumentException("El nivel de confianza debe estar entre 0.0 y 1.0");
        }
    }
}
