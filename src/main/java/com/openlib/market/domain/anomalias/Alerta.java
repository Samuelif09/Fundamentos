package com.openlib.market.domain.anomalias;

import java.time.LocalDateTime;
import java.util.UUID;

public class Alerta {
    private final String id;
    private final ReglaAnomalia reglaInfringida;
    private final double valorRegistrado;
    private final LocalDateTime fechaGeneracion;
    private EstadoAlerta estado;

    public Alerta(ReglaAnomalia reglaInfringida, double valorRegistrado) {
        if (reglaInfringida == null) {
            throw new IllegalArgumentException("La regla infringida no puede ser nula");
        }
        this.id = UUID.randomUUID().toString();
        this.reglaInfringida = reglaInfringida;
        this.valorRegistrado = valorRegistrado;
        this.fechaGeneracion = LocalDateTime.now();
        this.estado = EstadoAlerta.CRITICO;
    }

    public String getId() {
        return id;
    }

    public ReglaAnomalia getReglaInfringida() {
        return reglaInfringida;
    }

    public double getValorRegistrado() {
        return valorRegistrado;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public EstadoAlerta getEstado() {
        return estado;
    }

    public void resolver() {
        this.estado = EstadoAlerta.RESUELTA;
    }
}
