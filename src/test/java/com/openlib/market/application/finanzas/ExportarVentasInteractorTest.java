package com.openlib.market.application.finanzas;

import com.openlib.market.domain.finanzas.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ExportarVentasInteractorTest {

    private ILiquidacionGateway liquidacionGateway;
    private IGeneradorReportesGateway generadorGateway;
    private ExportarVentasInteractor interactor;

    @BeforeEach
    void setUp() {
        liquidacionGateway = mock(ILiquidacionGateway.class);
        generadorGateway = mock(IGeneradorReportesGateway.class);
        interactor = new ExportarVentasInteractor(liquidacionGateway, generadorGateway);
    }

    @Test
    void debeGenerarReporteConDatos() {
        LocalDate hoy = LocalDate.now();
        when(liquidacionGateway.obtenerTransaccionesPorVendedor("seller-1")).thenReturn(List.of(
                new TransaccionFinanciera("t1", 100.0, hoy),
                new TransaccionFinanciera("t2", 50.0, hoy)
        ));

        when(generadorGateway.generar(any(MatrizReporte.class), eq(FormatoExportacion.CSV)))
                .thenReturn("CSV_DATA".getBytes());

        ReporteExportable reporte = interactor.exportar("seller-1", hoy.minusDays(1), hoy.plusDays(1), "csv");

        assertNotNull(reporte);
        assertEquals(FormatoExportacion.CSV, reporte.getFormato());
        assertEquals("CSV_DATA", new String(reporte.getContenido()));
        
        verify(generadorGateway).generar(argThat(matriz -> matriz.getFilas().size() == 2), eq(FormatoExportacion.CSV));
    }

    @Test
    void debeGenerarReporteConFilaUnicaSiNoHayTransacciones() {
        LocalDate hoy = LocalDate.now();
        when(liquidacionGateway.obtenerTransaccionesPorVendedor("seller-1")).thenReturn(List.of());

        when(generadorGateway.generar(any(MatrizReporte.class), eq(FormatoExportacion.EXCEL)))
                .thenReturn("EXCEL_DATA".getBytes());

        ReporteExportable reporte = interactor.exportar("seller-1", hoy.minusDays(1), hoy.plusDays(1), "excel");

        verify(generadorGateway).generar(argThat(matriz -> 
            matriz.getFilas().size() == 1 && matriz.getFilas().get(0).get(0).equals("Sin movimientos")
        ), eq(FormatoExportacion.EXCEL));
    }
}
