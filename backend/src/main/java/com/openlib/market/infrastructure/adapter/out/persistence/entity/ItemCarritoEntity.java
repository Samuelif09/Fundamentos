package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.util.UUID;

@Entity
@Table(name = "items_carrito")
public class ItemCarritoEntity {

    @Id
    private String id;

    @ManyToOne // FIX: Configura la relación bidireccional hacia el padre
    @JoinColumn(name = "sesion_id", nullable = false)
    private CarritoEntity carrito;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private int cantidad;

    public ItemCarritoEntity() {
        this.id = UUID.randomUUID().toString(); // FIX: Autogenerar el ID por defecto para Hibernate
    }

    public ItemCarritoEntity(CarritoEntity carrito, String isbn, int cantidad) {
        this.id = UUID.randomUUID().toString();
        this.carrito = carrito;
        this.isbn = isbn;
        this.cantidad = cantidad;
    }

    public String getId() { return id; }
    public CarritoEntity getCarrito() { return carrito; }
    public void setCarrito(CarritoEntity carrito) { this.carrito = carrito; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
