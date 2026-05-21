package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.util.UUID;

@Entity
@Table(name = "items_pedido")
public class ItemPedidoEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private PedidoEntity pedido;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private double precioUnitario;

    public ItemPedidoEntity() {}

    public ItemPedidoEntity(PedidoEntity pedido, String isbn, int cantidad, double precioUnitario) {
        this.id = UUID.randomUUID().toString();
        this.pedido = pedido;
        this.isbn = isbn;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public String getId() { return id; }
    public PedidoEntity getPedido() { return pedido; }
    public String getIsbn() { return isbn; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }

    public void setPedido(PedidoEntity pedido) { this.pedido = pedido; }
}
