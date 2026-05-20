package com.openlib.market.domain.pago;

import java.util.UUID;

public class Pedido {
    private String id;
    private String sesionId;
    private String idUsuario;
    private double total;
    private EstadoPedido estado;
    private java.time.LocalDateTime fecha;
    private TipoMetodoPago tipoMetodoPago;

    public Pedido(String sesionId, double total, TipoMetodoPago tipoMetodoPago) {
        if (sesionId == null || sesionId.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de sesión es requerido para el pedido");
        }
        if (total <= 0) {
            throw new IllegalArgumentException("El total del pedido debe ser mayor a cero");
        }
        this.id = UUID.randomUUID().toString();
        this.sesionId = sesionId;
        this.total = total;
        this.estado = EstadoPedido.PENDIENTE;
        this.fecha = java.time.LocalDateTime.now();
        this.tipoMetodoPago = tipoMetodoPago;
    }

    public Pedido(String id, String sesionId, String idUsuario, double total, EstadoPedido estado, java.time.LocalDateTime fecha, TipoMetodoPago tipoMetodoPago) {
        this.id = id;
        this.sesionId = sesionId;
        this.idUsuario = idUsuario;
        this.total = total;
        this.estado = estado;
        this.fecha = fecha;
        this.tipoMetodoPago = tipoMetodoPago;
    }

    public String getId() { return id; }
    public String getSesionId() { return sesionId; }
    public String getIdUsuario() { return idUsuario; }
    public double getTotal() { return total; }
    public EstadoPedido getEstado() { return estado; }
    public java.time.LocalDateTime getFecha() { return fecha; }
    public TipoMetodoPago getTipoMetodoPago() { return tipoMetodoPago; }

    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public void marcarComoPagado() {
        if (this.estado != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException("Solo se puede pagar un pedido pendiente");
        }
        this.estado = EstadoPedido.PAGADO;
    }

    public void marcarComoFallido() {
        this.estado = EstadoPedido.FALLIDO;
    }
}
