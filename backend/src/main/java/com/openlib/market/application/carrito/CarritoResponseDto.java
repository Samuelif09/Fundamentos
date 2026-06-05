package com.openlib.market.application.carrito;

import java.util.List;

public class CarritoResponseDto {
    private String sesionId;
    private List<CarritoItemDto> items;
    private double total;

    public CarritoResponseDto(String sesionId, List<CarritoItemDto> items, double total) {
        this.sesionId = sesionId;
        this.items = items;
        this.total = total;
    }

    public String getSesionId() { return sesionId; }
    public void setSesionId(String sesionId) { this.sesionId = sesionId; }

    public List<CarritoItemDto> getItems() { return items; }
    public void setItems(List<CarritoItemDto> items) { this.items = items; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
