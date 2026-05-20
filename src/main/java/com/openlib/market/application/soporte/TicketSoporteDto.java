package com.openlib.market.application.soporte;

public class TicketSoporteDto {
    private final String id;
    private final String idUsuario;
    private final String asunto;
    private final String descripcion;
    private final String estado;
    private final String prioridad;
    private final String fechaCreacion;

    public TicketSoporteDto(String id, String idUsuario, String asunto, String descripcion, String estado, String prioridad, String fechaCreacion) {
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
    public String getEstado() { return estado; }
    public String getPrioridad() { return prioridad; }
    public String getFechaCreacion() { return fechaCreacion; }
}
