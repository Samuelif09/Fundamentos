package com.openlib.market.application.finanzas;

import com.openlib.market.domain.finanzas.AgrupacionTiempo;
import com.openlib.market.domain.finanzas.ComisionFactory;
import com.openlib.market.domain.finanzas.IComisionStrategy;
import com.openlib.market.domain.finanzas.IVentasReadGateway;
import com.openlib.market.domain.finanzas.VentaPlanaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GenerarMetricasVentasVendedorInteractorTest {

    private IVentasReadGateway ventasReadGateway;
    private ComisionFactory comisionFactory;
    private GenerarMetricasVentasVendedorInteractor interactor;

    @BeforeEach
    void setUp() {
        ventasReadGateway = mock(IVentasReadGateway.class);
        comisionFactory = mock(ComisionFactory.class);
        interactor = new GenerarMetricasVentasVendedorInteractor(ventasReadGateway, comisionFactory);
    }

    @Test
    void testAgrupacionDiaria() {
        LocalDate desde = LocalDate.of(2026, 6, 1);
        LocalDate hasta = LocalDate.of(2026, 6, 2);

        // 2 ventas el dia 1
        VentaPlanaDto v1 = new VentaPlanaDto("Libro", new BigDecimal("10.00"), 2, LocalDateTime.of(2026, 6, 1, 10, 0));
        VentaPlanaDto v2 = new VentaPlanaDto("Libro", new BigDecimal("20.00"), 1, LocalDateTime.of(2026, 6, 1, 15, 0));
        // 1 venta el dia 2
        VentaPlanaDto v3 = new VentaPlanaDto("Audiolibro", new BigDecimal("15.00"), 2, LocalDateTime.of(2026, 6, 2, 9, 0));

        when(ventasReadGateway.obtenerVentasPorVendedorYFechas("v1", desde, hasta))
                .thenReturn(Arrays.asList(v1, v2, v3));

        IComisionStrategy strategyLibro = mock(IComisionStrategy.class);
        IComisionStrategy strategyAudiolibro = mock(IComisionStrategy.class);

        when(comisionFactory.obtenerEstrategia("Libro")).thenReturn(strategyLibro);
        when(comisionFactory.obtenerEstrategia("Audiolibro")).thenReturn(strategyAudiolibro);

        // Comision libro 10%
        when(strategyLibro.calcularComision(new BigDecimal("10.00"))).thenReturn(new BigDecimal("1.00")); // v1: comision = 1.00
        when(strategyLibro.calcularComision(new BigDecimal("20.00"))).thenReturn(new BigDecimal("2.00")); // v2: comision = 2.00
        // Comision audiolibro 20%
        when(strategyAudiolibro.calcularComision(new BigDecimal("15.00"))).thenReturn(new BigDecimal("3.00")); // v3: comision = 3.00

        List<MetricaTemporalResponseDto> result = interactor.ejecutar("v1", desde, hasta, AgrupacionTiempo.DIARIA);

        assertEquals(2, result.size());
        
        // Dia 1: v1 + v2.
        // v1: 2 * 10 = 20. Comision = 2 * 1.00 = 2. Neto = 18.
        // v2: 1 * 20 = 20. Comision = 1 * 2.00 = 2. Neto = 18.
        // Total dia 1: Items=3, Neto=36.00
        assertEquals("2026-06-01", result.get(0).getPeriodo());
        assertEquals(3, result.get(0).getCantidadItemsVendidos());
        assertEquals(new BigDecimal("36.00"), result.get(0).getIngresoNetoVendedor());

        // Dia 2: v3.
        // v3: 2 * 15 = 30. Comision = 2 * 3.00 = 6. Neto = 24.00
        // Total dia 2: Items=2, Neto=24.00
        assertEquals("2026-06-02", result.get(1).getPeriodo());
        assertEquals(2, result.get(1).getCantidadItemsVendidos());
        assertEquals(new BigDecimal("24.00"), result.get(1).getIngresoNetoVendedor());
    }
}
