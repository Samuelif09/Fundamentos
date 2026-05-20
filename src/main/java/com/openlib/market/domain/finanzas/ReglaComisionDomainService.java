package com.openlib.market.domain.finanzas;

public class ReglaComisionDomainService {
    private static final double TASA_COMISION_PLATAFORMA = 0.15; // 15%
    private static final double TASA_IMPUESTOS = 0.0; // Simplificado por ahora (0%)

    public DesgloseFinanciero calcularDesglose(double montoBruto) {
        double comision = montoBruto * TASA_COMISION_PLATAFORMA;
        double impuestos = montoBruto * TASA_IMPUESTOS;
        double gananciaNeta = montoBruto - comision - impuestos;

        return new DesgloseFinanciero(montoBruto, comision, impuestos, gananciaNeta);
    }
}
