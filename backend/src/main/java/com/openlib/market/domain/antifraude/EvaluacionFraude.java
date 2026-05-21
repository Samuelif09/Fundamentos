package com.openlib.market.domain.antifraude;

import java.time.LocalDateTime;
import java.util.UUID;

public class EvaluacionFraude {
    private final String id;
    private final String idPedido;
    private final RiesgoTransaccion riesgo;
    private final MotivoAlerta motivo;
    private final LocalDateTime fechaEvaluacion;
    private final boolean requiereBloqueo;

    public EvaluacionFraude(String idPedido, RiesgoTransaccion riesgo, MotivoAlerta motivo) {
        if (idPedido == null || idPedido.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del pedido es obligatorio");
        }
        if (riesgo == null) throw new IllegalArgumentException("El riesgo es obligatorio");
        if (motivo == null) throw new IllegalArgumentException("El motivo es obligatorio");

        this.id = UUID.randomUUID().toString();
        this.idPedido = idPedido;
        this.riesgo = riesgo;
        this.motivo = motivo;
        this.fechaEvaluacion = LocalDateTime.now();
        
        // Regla de negocio
        this.requiereBloqueo = riesgo.valor() >= 90;
    }

    public String getId() { return id; }
    public String getIdPedido() { return idPedido; }
    public RiesgoTransaccion getRiesgo() { return riesgo; }
    public MotivoAlerta getMotivo() { return motivo; }
    public LocalDateTime getFechaEvaluacion() { return fechaEvaluacion; }
    public boolean requiereBloqueo() { return requiereBloqueo; }
}
