package com.openlib.market.domain.resena;

public class ComentarioRespuesta {
    private static final int MAX_CHARS = 500;
    private final String texto;

    public ComentarioRespuesta(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("La respuesta no puede estar vacía");
        }
        if (texto.length() > MAX_CHARS) {
            throw new IllegalArgumentException("La respuesta no puede superar " + MAX_CHARS + " caracteres");
        }
        this.texto = texto;
    }

    public String getTexto() { return texto; }
}
