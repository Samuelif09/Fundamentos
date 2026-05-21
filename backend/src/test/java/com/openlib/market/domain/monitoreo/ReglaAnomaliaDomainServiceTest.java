package com.openlib.market.domain.monitoreo;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class ReglaAnomaliaDomainServiceTest {

    private final ReglaAnomaliaDomainService service = new ReglaAnomaliaDomainService();

    @Test
    void debeGenerarAlertaSiValorSuperaUmbral() {
        ReglaAnomalia regla = new ReglaAnomalia(MetricaObjetivo.FALLOS_PAGO, new UmbralCritico(15.0));
        
        Optional<Alerta> alerta = service.evaluar(regla, 20.0);
        
        assertTrue(alerta.isPresent());
        assertEquals(regla.getId(), alerta.get().getIdRegla());
        assertEquals(20.0, alerta.get().getValorRegistrado());
    }

    @Test
    void noDebeGenerarAlertaSiValorEstaPorDebajo() {
        ReglaAnomalia regla = new ReglaAnomalia(MetricaObjetivo.FALLOS_PAGO, new UmbralCritico(15.0));
        
        Optional<Alerta> alerta = service.evaluar(regla, 10.0);
        
        assertTrue(alerta.isEmpty());
    }

    @Test
    void noDebeGenerarAlertaSiReglaInactiva() {
        ReglaAnomalia regla = new ReglaAnomalia("r1", MetricaObjetivo.FALLOS_PAGO, new UmbralCritico(15.0), false);
        
        Optional<Alerta> alerta = service.evaluar(regla, 20.0);
        
        assertTrue(alerta.isEmpty());
    }
}
