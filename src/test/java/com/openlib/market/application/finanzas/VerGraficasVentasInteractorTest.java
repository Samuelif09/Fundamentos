package com.openlib.market.application.finanzas;

import com.openlib.market.domain.finanzas.ILiquidacionGateway;
import com.openlib.market.domain.finanzas.IntervaloTiempo;
import com.openlib.market.domain.finanzas.PuntoDatos;
import com.openlib.market.domain.finanzas.TransaccionFinanciera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VerGraficasVentasInteractorTest {

    private ILiquidacionGateway liquidacionGateway;
    private VerGraficasVentasInteractor interactor;

    @BeforeEach
    void setUp() {
        liquidacionGateway = mock(ILiquidacionGateway.class);
        interactor = new VerGraficasVentasInteractor(liquidacionGateway);
    }

    @Test
    void debeRellenarDiasVaciosEnIntervaloDiario() {
        LocalDate lunes = LocalDate.of(2023, 10, 2);
        LocalDate miercoles = LocalDate.of(2023, 10, 4);

        when(liquidacionGateway.obtenerTransaccionesPorVendedor("seller-1")).thenReturn(List.of(
                new TransaccionFinanciera("t1", 100.0, lunes),
                new TransaccionFinanciera("t2", 50.0, miercoles)
        ));

        List<PuntoDatos> serie = interactor.verGrafica("seller-1", IntervaloTiempo.DIARIO);

        assertEquals(3, serie.size());
        
        assertEquals("2023-10-02", serie.get(0).getFecha());
        assertEquals(100.0, serie.get(0).getValor());

        // El martes debe estar rellenado con 0
        assertEquals("2023-10-03", serie.get(1).getFecha());
        assertEquals(0.0, serie.get(1).getValor());

        assertEquals("2023-10-04", serie.get(2).getFecha());
        assertEquals(50.0, serie.get(2).getValor());
    }

    @Test
    void debeAgruparEnIntervaloMensual() {
        LocalDate oct1 = LocalDate.of(2023, 10, 1);
        LocalDate oct20 = LocalDate.of(2023, 10, 20);
        LocalDate dic1 = LocalDate.of(2023, 12, 1);

        when(liquidacionGateway.obtenerTransaccionesPorVendedor("seller-1")).thenReturn(List.of(
                new TransaccionFinanciera("t1", 10.0, oct1),
                new TransaccionFinanciera("t2", 20.0, oct20),
                new TransaccionFinanciera("t3", 50.0, dic1)
        ));

        List<PuntoDatos> serie = interactor.verGrafica("seller-1", IntervaloTiempo.MENSUAL);

        assertEquals(3, serie.size()); // Oct, Nov, Dic

        assertEquals("2023-10", serie.get(0).getFecha());
        assertEquals(30.0, serie.get(0).getValor());

        // Noviembre debe ser 0
        assertEquals("2023-11", serie.get(1).getFecha());
        assertEquals(0.0, serie.get(1).getValor());

        assertEquals("2023-12", serie.get(2).getFecha());
        assertEquals(50.0, serie.get(2).getValor());
    }
}
