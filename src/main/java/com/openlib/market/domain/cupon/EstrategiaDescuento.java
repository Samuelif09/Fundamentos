package com.openlib.market.domain.cupon;

/**
 * Strategy Pattern: Cada implementación define cómo se calcula el descuento.
 * OCP: se pueden añadir nuevas estrategias sin modificar el código existente.
 */
public interface EstrategiaDescuento {
    double aplicar(double totalOriginal);
}
