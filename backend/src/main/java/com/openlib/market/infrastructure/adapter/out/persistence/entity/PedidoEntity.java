package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
public class PedidoEntity {

    @Id
    private String id;
    
    @Column(nullable = false)
    private String sesionId;
    
    private String idUsuario;
    
    @Column(nullable = false)
    private double total;
    
    @Column(nullable = false)
    private String estado;
    
    @Column(nullable = false)
    private LocalDateTime fecha;
    
    private String tipoMetodoPago;

    public PedidoEntity() {}

    public PedidoEntity(String id, String sesionId, String idUsuario, double total, String estado, LocalDateTime fecha, String tipoMetodoPago) {
        this.id = id;
        this.sesionId = sesionId;
        this.idUsuario = idUsuario;
        this.total = total;
        this.estado = estado;
        this.fecha = fecha;
        this.tipoMetodoPago = tipoMetodoPago;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSesionId() { return sesionId; }
    public void setSesionId(String sesionId) { this.sesionId = sesionId; }

    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getTipoMetodoPago() { return tipoMetodoPago; }
    public void setTipoMetodoPago(String tipoMetodoPago) { this.tipoMetodoPago = tipoMetodoPago; }

    @jakarta.persistence.OneToMany(mappedBy = "pedido", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private java.util.List<ItemPedidoEntity> items = new java.util.ArrayList<>();

    public java.util.List<ItemPedidoEntity> getItems() { return items; }
    public void setItems(java.util.List<ItemPedidoEntity> items) { this.items = items; }
    public void addItem(ItemPedidoEntity item) {
        items.add(item);
        item.setPedido(this);
    }
}
