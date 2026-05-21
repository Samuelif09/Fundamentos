package com.openlib.market.domain.chatbot;

import java.util.UUID;

public class SesionChatbot {
    private final String id;
    private final String idUsuario;
    private EstadoSesion estado;
    private final String ultimoMensajeUsuario;
    private String respuestaActualBot;

    public SesionChatbot(String idUsuario, String ultimoMensajeUsuario) {
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio");
        }
        if (ultimoMensajeUsuario == null || ultimoMensajeUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje es obligatorio");
        }
        
        this.id = UUID.randomUUID().toString();
        this.idUsuario = idUsuario;
        this.ultimoMensajeUsuario = ultimoMensajeUsuario;
        this.estado = EstadoSesion.ACTIVA;
    }

    public String getId() { return id; }
    public String getIdUsuario() { return idUsuario; }
    public EstadoSesion getEstado() { return estado; }
    public String getUltimoMensajeUsuario() { return ultimoMensajeUsuario; }
    public String getRespuestaActualBot() { return respuestaActualBot; }

    public void procesarRespuesta(RespuestaBot respuesta) {
        if (respuesta == null) throw new IllegalArgumentException("La respuesta no puede ser nula");
        
        if (respuesta.nivelConfianza() < 0.6) {
            this.estado = EstadoSesion.ESCALADA_A_HUMANO;
            this.respuestaActualBot = "No he entendido completamente tu consulta. Te transferiré con un agente.";
        } else {
            this.estado = EstadoSesion.ACTIVA;
            this.respuestaActualBot = respuesta.texto();
        }
    }
    
    public void resolver() {
        this.estado = EstadoSesion.RESUELTA;
    }
}
