package com.openlib.market.application.carrito;

import java.util.List;

public class CartDto {
    private final List<CartItemDto> items;
    private final double subtotal;
    private final double taxes;
    private final double total;

    public CartDto(List<CartItemDto> items, double subtotal, double taxes, double total) {
        this.items = items;
        this.subtotal = subtotal;
        this.taxes = taxes;
        this.total = total;
    }

    public List<CartItemDto> getItems() { return items; }
    public double getSubtotal() { return subtotal; }
    public double getTaxes() { return taxes; }
    public double getTotal() { return total; }
}
