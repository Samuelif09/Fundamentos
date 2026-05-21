package com.openlib.market.domain.reembolso;

import com.openlib.market.domain.pago.Pedido;
import java.util.UUID;

public class SolicitudReembolso {
    private final String id;
    private final String idPedido;
    private final double montoReembolso;
    private final String motivo;
    private EstadoReembolso estado;

    public SolicitudReembolso(String idPedido, double montoReembolso, String motivo, Pedido pedidoOriginal) {
        if (idPedido == null || idPedido.trim().isEmpty()) throw new IllegalArgumentException("ID de pedido obligatorio");
        if (motivo == null || motivo.trim().isEmpty()) throw new IllegalArgumentException("El motivo es obligatorio");
        if (montoReembolso <= 0) throw new IllegalArgumentException("El monto debe ser mayor a 0");
        
        if (pedidoOriginal != null && montoReembolso > pedidoOriginal.getTotal()) {
            throw new MontoReembolsoInvalidoException("El monto del reembolso no puede superar el total del pedido");
        }

        this.id = UUID.randomUUID().toString();
        this.idPedido = idPedido;
        this.montoReembolso = montoReembolso;
        this.motivo = motivo;
        this.estado = EstadoReembolso.PENDIENTE;
    }

    public SolicitudReembolso(String id, String idPedido, double montoReembolso, String motivo, EstadoReembolso estado) {
        this.id = id;
        this.idPedido = idPedido;
        this.montoReembolso = montoReembolso;
        this.motivo = motivo;
        this.estado = estado;
    }

    public String getId() { return id; }
    public String getIdPedido() { return idPedido; }
    public double getMontoReembolso() { return montoReembolso; }
    public String getMotivo() { return motivo; }
    public EstadoReembolso getEstado() { return estado; }

    public void aprobar() {
        if (this.estado != EstadoReembolso.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden aprobar reembolsos pendientes");
        }
        this.estado = EstadoReembolso.APROBADO;
    }

    public void denegar() {
        if (this.estado != EstadoReembolso.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden denegar reembolsos pendientes");
        }
        this.estado = EstadoReembolso.DENEGADO;
    }
}
