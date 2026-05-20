package com.openlib.market.domain.carrito;

import java.util.List;

public class DescuentoFijoDecorator extends CalculadorSubtotalDecorator {
    private final double descuentoMonto;
    private final double limiteMinimo;

    public DescuentoFijoDecorator(ICalculadorSubtotal calculadorDecorado, double descuentoMonto, double limiteMinimo) {
        super(calculadorDecorado);
        this.descuentoMonto = descuentoMonto;
        this.limiteMinimo = limiteMinimo;
    }

    @Override
    public double calcular(List<ItemCarrito> items) {
        double subtotalBase = super.calcular(items);
        if (subtotalBase >= limiteMinimo) {
            double subtotalConDescuento = subtotalBase - descuentoMonto;
            return Math.max(0, subtotalConDescuento); // No puede ser negativo
        }
        return subtotalBase;
    }
}
