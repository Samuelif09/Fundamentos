package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.finanzas.TransaccionFinanciera;
import com.openlib.market.infrastructure.adapter.out.persistence.PersistenceTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@ActiveProfiles("test")
public class LiquidacionJpaGatewayTest {

    @Autowired
    private LiquidacionJpaGateway gateway;

    @Test
    public void testSumaDeIngresosPorPeriodo() {
        LocalDate hoy = LocalDate.now();

        // Insertar 3 transacciones para el mismo vendedor en distintas fechas
        gateway.guardar("vendedor-1", 100.0, hoy.minusDays(10)); // dentro del periodo
        gateway.guardar("vendedor-1", 200.0, hoy.minusDays(5));  // dentro del periodo
        gateway.guardar("vendedor-1", 50.0,  hoy.plusDays(5));   // fuera del periodo (futuro)
        gateway.guardar("vendedor-2", 999.0, hoy.minusDays(7));  // otro vendedor, no debe contar

        // Periodo: últimos 15 días hasta hoy
        double suma = gateway.sumarIngresosPorPeriodo("vendedor-1", hoy.minusDays(15), hoy);

        assertEquals(300.0, suma, 0.001, "La suma debe ser 100 + 200 = 300");
    }

    @Test
    public void testFiltradoPorPeriodoRetornaListaCorrecta() {
        LocalDate hoy = LocalDate.now();

        gateway.guardar("vendedor-3", 75.0, hoy.minusDays(3));
        gateway.guardar("vendedor-3", 25.0, hoy.minusDays(1));
        gateway.guardar("vendedor-3", 500.0, hoy.minusDays(30)); // fuera del rango de 7 días

        List<TransaccionFinanciera> resultado = gateway.obtenerTransaccionesPorPeriodo(
                "vendedor-3", hoy.minusDays(7), hoy);

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(t -> !t.getFecha().isBefore(hoy.minusDays(7))));
    }
}
