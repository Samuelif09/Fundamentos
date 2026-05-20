package com.openlib.market.domain.soporte;

public interface IReporteGateway {
    void guardar(ReporteContenido reporte);
    boolean existeReportePendiente(String idDenunciante, String idElemento);
}
