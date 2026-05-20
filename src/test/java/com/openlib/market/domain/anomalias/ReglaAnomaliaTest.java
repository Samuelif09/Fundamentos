package com.openlib.market.domain.anomalias;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReglaAnomaliaTest {

    @Test
    void debeCrearReglaAnomaliaSiValoresSonValidos() {
        UmbralCritico umbral = new UmbralCritico(15.0);
        ReglaAnomalia regla = new ReglaAnomalia(MetricaObjetivo.FALLOS_PAGO, umbral);

        assertNotNull(regla);
        assertEquals(MetricaObjetivo.FALLOS_PAGO, regla.getMetricaObjetivo());
        assertEquals(15.0, regla.getUmbral().valor());
    }

    @Test
    void debeEvaluarComoAnomaliaSiValorSuperaUmbral() {
        UmbralCritico umbral = new UmbralCritico(15.0);
        ReglaAnomalia regla = new ReglaAnomalia(MetricaObjetivo.FALLOS_PAGO, umbral);

        boolean esAnomalia = regla.evaluar(20.0);
        assertTrue(esAnomalia, "Debería ser anomalía porque 20.0 supera el umbral de 15.0");
    }

    @Test
    void noDebeEvaluarComoAnomaliaSiValorEsMenorOIgualAlUmbral() {
        UmbralCritico umbral = new UmbralCritico(15.0);
        ReglaAnomalia regla = new ReglaAnomalia(MetricaObjetivo.FALLOS_PAGO, umbral);

        boolean esAnomalia = regla.evaluar(10.0);
        assertFalse(esAnomalia, "No debería ser anomalía porque 10.0 no supera el umbral de 15.0");

        boolean esAnomaliaIgual = regla.evaluar(15.0);
        assertFalse(esAnomaliaIgual, "No debería ser anomalía porque 15.0 es igual al umbral y la regla suele ser > umbral");
    }
}
