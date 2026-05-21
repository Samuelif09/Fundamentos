package com.openlib.market.domain.soporte;

import java.time.LocalDateTime;

public class TicketSoporte {
    private final String id;
    private final String idUsuario;
    private final String asunto;
    private final String descripcion;
    private final EstadoTicket estado;
    private final Prioridad prioridad;
    private final LocalDateTime fechaCreacion;

    public TicketSoporte(String id, String idUsuario, String asunto, String descripcion, EstadoTicket estado, Prioridad prioridad, LocalDateTime fechaCreacion) {
        if (asunto == null || asunto.trim().isEmpty()) {
            throw new IllegalArgumentException("El asunto del ticket es obligatorio.");
        }
        this.id = id;
        this.idUsuario = idUsuario;
        this.asunto = asunto;
        this.descripcion = descripcion;
        this.estado = estado;
        this.prioridad = prioridad;
        this.fechaCreacion = fechaCreacion;
    }

    public String getId() { return id; }
    public String getIdUsuario() { return idUsuario; }
    public String getAsunto() { return asunto; }
    public String getDescripcion() { return descripcion; }
    public EstadoTicket getEstado() { return estado; }
    public Prioridad getPrioridad() { return prioridad; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}
