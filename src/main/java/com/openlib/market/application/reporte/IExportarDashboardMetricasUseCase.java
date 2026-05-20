package com.openlib.market.application.reporte;

public interface IExportarDashboardMetricasUseCase {
    byte[] exportarReporte(String tipoStr, String formatoStr);
}
