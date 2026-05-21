package com.openlib.market.domain.antifraude;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EvaluacionFraudeTest {

    @Test
    void debeRequerirBloqueoSiRiesgoEsAlto() {
        EvaluacionFraude eval = new EvaluacionFraude("ped1", new RiesgoTransaccion(95), MotivoAlerta.TARJETA_REPORTADA);
        assertTrue(eval.requiereBloqueo());
    }

    @Test
    void noDebeRequerirBloqueoSiRiesgoEsBajo() {
        EvaluacionFraude eval = new EvaluacionFraude("ped1", new RiesgoTransaccion(20), MotivoAlerta.NINGUNO);
        assertFalse(eval.requiereBloqueo());
    }

    @Test
    void debeValidarRangoRiesgo() {
        assertThrows(IllegalArgumentException.class, () -> new RiesgoTransaccion(-1));
        assertThrows(IllegalArgumentException.class, () -> new RiesgoTransaccion(101));
    }
}
