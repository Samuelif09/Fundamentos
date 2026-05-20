package com.openlib.market.domain.carrito;

import java.util.List;

public interface ICalculadorSubtotal {
    double calcular(List<ItemCarrito> items);
}
