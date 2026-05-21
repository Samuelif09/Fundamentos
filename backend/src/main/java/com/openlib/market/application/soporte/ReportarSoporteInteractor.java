package com.openlib.market.application.soporte;

import com.openlib.market.domain.soporte.IReporteGateway;
import com.openlib.market.domain.soporte.ReporteContenido;
import com.openlib.market.domain.soporte.ReporteDuplicadoException;

public class ReportarSoporteInteractor implements IReportarSoporteUseCase {

    private final IReporteGateway reporteGateway;

    public ReportarSoporteInteractor(IReporteGateway reporteGateway) {
        this.reporteGateway = reporteGateway;
    }

    @Override
    public void reportar(ReportarSoporteRequestDto request) {
        // 1. Validar que no haya reportes pendientes duplicados
        boolean yaReportado = reporteGateway.existeReportePendiente(request.getIdDenunciante(), request.getIdElemento());
        if (yaReportado) {
            throw new ReporteDuplicadoException("Ya has reportado este elemento y está pendiente de revisión.");
        }

        // 2. Crear la entidad de dominio
        ReporteContenido reporte = new ReporteContenido(
                request.getIdDenunciante(),
                request.getTipoElemento(),
                request.getIdElemento(),
                request.getMotivo()
        );

        // 3. Persistir
        reporteGateway.guardar(reporte);
    }
}
