package com.openlib.market.application.finanzas;

import com.openlib.market.domain.finanzas.ReporteExportable;
import java.time.LocalDate;

public interface IExportarVentasUseCase {
    ReporteExportable exportar(String idVendedor, LocalDate desde, LocalDate hasta, String formatoStr);
}
