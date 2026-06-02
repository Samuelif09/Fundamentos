package com.openlib.market.infrastructure.inventario;

public class AbastecerInventarioRequestDto {
    private int cantidad;

    public AbastecerInventarioRequestDto() {}

    public AbastecerInventarioRequestDto(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
