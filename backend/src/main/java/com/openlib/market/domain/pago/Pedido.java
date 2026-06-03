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
    private java.util.List<ItemPedido> items = new java.util.ArrayList<>();

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

    // Backward-compatible overload (no items)
    public Pedido(String id, String sesionId, String idUsuario, double total, EstadoPedido estado, java.time.LocalDateTime fecha, TipoMetodoPago tipoMetodoPago) {
        this(id, sesionId, idUsuario, total, estado, fecha, tipoMetodoPago, null);
    }

    public Pedido(String id, String sesionId, String idUsuario, double total, EstadoPedido estado, java.time.LocalDateTime fecha, TipoMetodoPago tipoMetodoPago, java.util.List<ItemPedido> items) {
        this.id = id;
        this.sesionId = sesionId;
        this.idUsuario = idUsuario;
        this.total = total;
        this.estado = estado;
        this.fecha = fecha;
        this.tipoMetodoPago = tipoMetodoPago;
        if (items != null) {
            this.items.addAll(items);
        }
    }

    public String getId() { return id; }
    public String getSesionId() { return sesionId; }
    public String getIdUsuario() { return idUsuario; }
    public double getTotal() { return total; }
    public EstadoPedido getEstado() { return estado; }
    public java.time.LocalDateTime getFecha() { return fecha; }
    public TipoMetodoPago getTipoMetodoPago() { return tipoMetodoPago; }
    public java.util.List<ItemPedido> getItems() { return items; }

    public void addItem(ItemPedido item) {
        this.items.add(item);
    }

    private com.openlib.market.domain.checkout.PedidoState pedidoState;

    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public void setPedidoState(com.openlib.market.domain.checkout.PedidoState pedidoState) {
        this.pedidoState = pedidoState;
    }

    public void procesarPago() {
        if (this.pedidoState == null) {
            if (this.estado == EstadoPedido.PAGADO) this.pedidoState = new com.openlib.market.domain.checkout.PagadoState();
            else if (this.estado == EstadoPedido.FALLIDO) this.pedidoState = new com.openlib.market.domain.checkout.FallidoState();
            else this.pedidoState = new com.openlib.market.domain.checkout.PendientePagoState();
        }
        this.pedidoState.procesarPago(this);
    }

    public void marcarComoPagado() {
        procesarPago();
    }

    public void marcarComoFallido() {
        this.estado = EstadoPedido.FALLIDO;
        this.pedidoState = new com.openlib.market.domain.checkout.FallidoState();
    }
}
