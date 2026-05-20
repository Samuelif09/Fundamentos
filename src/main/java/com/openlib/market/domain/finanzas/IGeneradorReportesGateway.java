package com.openlib.market.domain.finanzas;

public interface IGeneradorReportesGateway {
    byte[] generar(MatrizReporte matriz, FormatoExportacion formato);
}
