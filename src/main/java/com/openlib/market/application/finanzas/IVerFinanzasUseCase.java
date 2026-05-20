package com.openlib.market.application.finanzas;

import java.time.LocalDate;

public interface IVerFinanzasUseCase {
    ReporteFinanzasDto obtenerReporteIngresos(String idVendedor, LocalDate inicio, LocalDate fin);
}
