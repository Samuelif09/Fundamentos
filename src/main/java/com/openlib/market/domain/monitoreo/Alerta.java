package com.openlib.market.domain.monitoreo;

import java.time.LocalDateTime;
import java.util.UUID;

public class Alerta {
    private final String id;
    private final String idRegla;
    private final double valorRegistrado;
    private final LocalDateTime fechaHora;
    private EstadoAlerta estado;

    public Alerta(String idRegla, double valorRegistrado) {
        this.id = UUID.randomUUID().toString();
        this.idRegla = idRegla;
        this.valorRegistrado = valorRegistrado;
        this.fechaHora = LocalDateTime.now();
        this.estado = EstadoAlerta.NUEVA;
    }

    public String getId() { return id; }
    public String getIdRegla() { return idRegla; }
    public double getValorRegistrado() { return valorRegistrado; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public EstadoAlerta getEstado() { return estado; }

    public void reconocer() {
        if (estado == EstadoAlerta.NUEVA) {
            this.estado = EstadoAlerta.RECONOCIDA;
        }
    }
}
