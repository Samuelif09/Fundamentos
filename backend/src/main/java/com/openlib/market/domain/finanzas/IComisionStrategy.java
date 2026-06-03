package com.openlib.market.domain.finanzas;
import java.math.BigDecimal;
public interface IComisionStrategy {
    BigDecimal calcularComision(BigDecimal precioOriginal);
}
