package com.openlib.market.domain.reporte;

public interface IGeneradorReportesGlobalGateway {
    byte[] generarReporte(ReportePlataforma reporte, FormatoReporte formato);
}
