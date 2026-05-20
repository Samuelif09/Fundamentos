package com.openlib.market.application.soporte;

import com.openlib.market.domain.soporte.ElementoReportado;
import com.openlib.market.domain.soporte.IReporteGateway;
import com.openlib.market.domain.soporte.ReporteContenido;
import com.openlib.market.domain.soporte.ReporteDuplicadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ReportarSoporteInteractorTest {

    private IReporteGateway reporteGateway;
    private ReportarSoporteInteractor interactor;

    @BeforeEach
    void setUp() {
        reporteGateway = mock(IReporteGateway.class);
        interactor = new ReportarSoporteInteractor(reporteGateway);
    }

    @Test
    void debeGuardarReporteSiNoEsDuplicado() {
        when(reporteGateway.existeReportePendiente("user1", "libro1")).thenReturn(false);

        ReportarSoporteRequestDto request = new ReportarSoporteRequestDto(
                "user1", ElementoReportado.LIBRO, "libro1", "Contenido ofensivo"
        );

        interactor.reportar(request);

        verify(reporteGateway, times(1)).guardar(any(ReporteContenido.class));
    }

    @Test
    void debeLanzarExcepcionSiEsReporteDuplicadoPendiente() {
        when(reporteGateway.existeReportePendiente("user1", "libro1")).thenReturn(true);

        ReportarSoporteRequestDto request = new ReportarSoporteRequestDto(
                "user1", ElementoReportado.LIBRO, "libro1", "Contenido ofensivo"
        );

        assertThrows(ReporteDuplicadoException.class, () -> interactor.reportar(request));
        
        verify(reporteGateway, never()).guardar(any(ReporteContenido.class));
    }
}
