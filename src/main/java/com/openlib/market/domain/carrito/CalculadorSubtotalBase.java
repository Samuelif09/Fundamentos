package com.openlib.market.domain.carrito;

import java.util.List;

public class CalculadorSubtotalBase implements ICalculadorSubtotal {
    @Override
    public double calcular(List<ItemCarrito> items) {
        return items.stream()
                .mapToDouble(ItemCarrito::getSubtotal)
                .sum();
    }
}
