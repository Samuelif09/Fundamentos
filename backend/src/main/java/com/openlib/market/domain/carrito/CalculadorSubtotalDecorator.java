package com.openlib.market.domain.carrito;

import java.util.List;

public abstract class CalculadorSubtotalDecorator implements ICalculadorSubtotal {
    protected final ICalculadorSubtotal calculadorDecorado;

    public CalculadorSubtotalDecorator(ICalculadorSubtotal calculadorDecorado) {
        this.calculadorDecorado = calculadorDecorado;
    }

    @Override
    public double calcular(List<ItemCarrito> items) {
        return calculadorDecorado.calcular(items);
    }
}
