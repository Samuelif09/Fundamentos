package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets_soporte")
public class TicketSoporteEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String idUsuario;

    @Column(nullable = false)
    private String asunto;

    @Column(length = 2000)
    private String descripcion;

    @Column(nullable = false)
    private String estado; // ABIERTO | EN_PROGRESO | CERRADO

    @Column(nullable = false)
    private String prioridad; // ALTA | MEDIA | BAJA

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    public TicketSoporteEntity() {}

    public TicketSoporteEntity(String id, String idUsuario, String asunto, String descripcion,
                               String estado, String prioridad, LocalDateTime fechaCreacion) {
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
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}
