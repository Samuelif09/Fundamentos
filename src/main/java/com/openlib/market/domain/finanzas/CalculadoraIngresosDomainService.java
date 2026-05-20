package com.openlib.market.domain.finanzas;

import java.util.List;

public class CalculadoraIngresosDomainService {

    public double calcularIngresos(List<TransaccionFinanciera> transacciones, Periodo periodo) {
        if (transacciones == null || transacciones.isEmpty()) {
            return 0.0;
        }

        double suma = transacciones.stream()
                .filter(t -> periodo.contiene(t.getFecha()))
                .mapToDouble(TransaccionFinanciera::getSubtotal)
                .sum();

        // Redondeo a 2 decimales para precisión financiera básica
        return Math.round(suma * 100.0) / 100.0;
    }
}
