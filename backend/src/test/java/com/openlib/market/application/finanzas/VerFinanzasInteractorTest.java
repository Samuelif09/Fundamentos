package com.openlib.market.application.finanzas;

import com.openlib.market.domain.finanzas.CalculadoraIngresosDomainService;
import com.openlib.market.domain.finanzas.ILiquidacionGateway;
import com.openlib.market.domain.finanzas.TransaccionFinanciera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class VerFinanzasInteractorTest {

    private ILiquidacionGateway liquidacionGateway;
    private CalculadoraIngresosDomainService calculadora;
    private VerFinanzasInteractor interactor;

    @BeforeEach
    void setUp() {
        liquidacionGateway = mock(ILiquidacionGateway.class);
        calculadora = new CalculadoraIngresosDomainService(); // Real impl para testear bien la suma
        interactor = new VerFinanzasInteractor(liquidacionGateway, calculadora);
    }

    @Test
    void debeCalcularIngresosCorrectamenteFiltrandoPorPeriodo() {
        LocalDate inicio = LocalDate.of(2023, 1, 1);
        LocalDate fin = LocalDate.of(2023, 1, 31);

        List<TransaccionFinanciera> transacciones = List.of(
                new TransaccionFinanciera("t1", 100.50, LocalDate.of(2023, 1, 15)), // Dentro
                new TransaccionFinanciera("t2", 50.25, LocalDate.of(2023, 1, 10)),  // Dentro
                new TransaccionFinanciera("t3", 200.0, LocalDate.of(2023, 2, 5))    // Fuera
        );

        when(liquidacionGateway.obtenerTransaccionesPorVendedor("seller-1")).thenReturn(transacciones);

        ReporteFinanzasDto reporte = interactor.obtenerReporteIngresos("seller-1", inicio, fin);

        assertEquals("seller-1", reporte.getIdVendedor());
        assertEquals(150.75, reporte.getIngresosTotales(), 0.001);
    }

    @Test
    void debeRetornarCeroSiNoHayTransaccionesEnElPeriodo() {
        LocalDate inicio = LocalDate.of(2023, 1, 1);
        LocalDate fin = LocalDate.of(2023, 1, 31);

        List<TransaccionFinanciera> transacciones = List.of(
                new TransaccionFinanciera("t3", 200.0, LocalDate.of(2023, 2, 5))    // Fuera
        );

        when(liquidacionGateway.obtenerTransaccionesPorVendedor("seller-1")).thenReturn(transacciones);

        ReporteFinanzasDto reporte = interactor.obtenerReporteIngresos("seller-1", inicio, fin);

        assertEquals(0.0, reporte.getIngresosTotales(), 0.001);
    }

    @Test
    void debeLanzarExcepcionSiFechaFinEsMenorAFechaInicio() {
        LocalDate inicio = LocalDate.of(2023, 2, 1);
        LocalDate fin = LocalDate.of(2023, 1, 31);

        assertThrows(IllegalArgumentException.class, () -> interactor.obtenerReporteIngresos("seller-1", inicio, fin));
    }
}
