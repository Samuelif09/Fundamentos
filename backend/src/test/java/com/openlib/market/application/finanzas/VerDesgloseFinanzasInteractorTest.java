package com.openlib.market.application.finanzas;

import com.openlib.market.domain.finanzas.DesgloseFinanciero;
import com.openlib.market.domain.finanzas.ILiquidacionGateway;
import com.openlib.market.domain.finanzas.ReglaComisionDomainService;
import com.openlib.market.domain.finanzas.TransaccionFinanciera;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VerDesgloseFinanzasInteractorTest {

    private ILiquidacionGateway liquidacionGateway;
    private ReglaComisionDomainService reglaComisionService;
    private VerDesgloseFinanzasInteractor interactor;

    @BeforeEach
    void setUp() {
        liquidacionGateway = mock(ILiquidacionGateway.class);
        reglaComisionService = new ReglaComisionDomainService(); // Prueba real de reglas matemáticas
        interactor = new VerDesgloseFinanzasInteractor(liquidacionGateway, reglaComisionService);
    }

    @Test
    void debeCalcularDesgloseParaMultiplesTransacciones() {
        when(liquidacionGateway.obtenerTransaccionesPorVendedor("seller-1")).thenReturn(List.of(
                new TransaccionFinanciera("t1", 100.0, LocalDate.now()),
                new TransaccionFinanciera("t2", 50.50, LocalDate.now())
        ));

        List<DesgloseFinancieroDto> resultados = interactor.obtenerDesglose("seller-1");

        assertEquals(2, resultados.size());

        // Test t1: 100.0 * 0.15 = 15.0 comision -> 85.0 neta
        assertEquals(100.0, resultados.get(0).getMontoBruto());
        assertEquals(15.0, resultados.get(0).getComisionPlataforma());
        assertEquals(85.0, resultados.get(0).getGananciaNeta());

        // Test t2: 50.50 * 0.15 = 7.575 (Redondeado a 7.58 o 7.57 dependiendo de la JVM. Nuestro redondeo usa Math.round: 7.575 * 100 = 757.5 -> 758 / 100 = 7.58)
        assertEquals(50.50, resultados.get(1).getMontoBruto());
        assertEquals(7.58, resultados.get(1).getComisionPlataforma(), 0.01);
        assertEquals(42.92, resultados.get(1).getGananciaNeta(), 0.01);
    }

    @Test
    void debeFallarSiLaEcuacionMatematicaNoCuadraEnElDominio() {
        assertThrows(IllegalStateException.class, () -> {
            // Fuerza una discrepancia manual
            new DesgloseFinanciero(100.0, 15.0, 0.0, 90.0);
        });
    }
}
