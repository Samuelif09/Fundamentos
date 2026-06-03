package com.openlib.market.domain.finanzas;
import java.math.BigDecimal;
import java.math.RoundingMode;
public class ComisionDigitalStrategy implements IComisionStrategy {
    private static final BigDecimal PORCENTAJE = new BigDecimal("0.20");
    @Override
    public BigDecimal calcularComision(BigDecimal precioOriginal) {
        return precioOriginal.multiply(PORCENTAJE).setScale(2, RoundingMode.HALF_UP);
    }
}
