package com.openlib.market.domain.comunicado;

import java.time.LocalDateTime;
import java.util.UUID;

public class ComunicadoMasivo {
    private final String id;
    private final String asunto;
    private final String cuerpoMensaje;
    private final FiltroDestinatarios filtro;
    private final LocalDateTime fechaEnvio;
    private int cantidadDestinatarios;

    public ComunicadoMasivo(String asunto, String cuerpoMensaje, FiltroDestinatarios filtro) {
        if (asunto == null || asunto.trim().isEmpty()) {
            throw new IllegalArgumentException("El asunto no puede estar vacío");
        }
        if (cuerpoMensaje == null || cuerpoMensaje.trim().isEmpty()) {
            throw new IllegalArgumentException("El cuerpo del mensaje no puede estar vacío");
        }
        if (filtro == null) {
            throw new IllegalArgumentException("Debe especificar un filtro de destinatarios");
        }
        
        this.id = UUID.randomUUID().toString();
        this.asunto = asunto;
        this.cuerpoMensaje = cuerpoMensaje;
        this.filtro = filtro;
        this.fechaEnvio = LocalDateTime.now();
        this.cantidadDestinatarios = 0;
    }

    public String getId() { return id; }
    public String getAsunto() { return asunto; }
    public String getCuerpoMensaje() { return cuerpoMensaje; }
    public FiltroDestinatarios getFiltro() { return filtro; }
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public int getCantidadDestinatarios() { return cantidadDestinatarios; }

    public void registrarEnvio(int cantidad) {
        this.cantidadDestinatarios = cantidad;
    }
}
