package com.openlib.market.domain.configuracion;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReglaComisionTest {

    @Test
    void debeCrearReglaGlobal() {
        ReglaComision regla = new ReglaComision("GLOBAL", 10.0);
        assertEquals("GLOBAL", regla.getIdCategoria());
        assertEquals(10.0, regla.getPorcentajeComision());
    }

    @Test
    void debeLanzarExcepcionSiPorcentajeInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new ReglaComision("FICCION", -5));
        assertThrows(IllegalArgumentException.class, () -> new ReglaComision("FICCION", 105));
    }

    @Test
    void debeCalcularComisionCorrectamente() {
        ReglaComision regla = new ReglaComision("FICCION", 15.0);
        double comision = regla.calcularComision(100.0);
        assertEquals(15.0, comision);
    }
}
