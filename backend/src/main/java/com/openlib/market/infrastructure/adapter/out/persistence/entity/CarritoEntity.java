package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "carritos")
public class CarritoEntity {

    @Id
    private String sesionId;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemCarritoEntity> items = new ArrayList<>();

    public CarritoEntity() {}

    public CarritoEntity(String sesionId) {
        this.sesionId = sesionId;
    }

    public String getSesionId() { return sesionId; }
    public void setSesionId(String sesionId) { this.sesionId = sesionId; }

    public List<ItemCarritoEntity> getItems() { return items; }
    public void setItems(List<ItemCarritoEntity> items) { this.items = items; }

    public void addItem(ItemCarritoEntity item) {
        this.items.add(item);
        item.setCarrito(this);
    }
}
