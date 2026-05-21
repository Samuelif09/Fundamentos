package com.openlib.market.domain.resena;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalificacionTest {

    @Test
    void debeCrearCalificacionValida() {
        Calificacion c = new Calificacion(4);
        assertEquals(4, c.getValor());
    }

    @Test
    void debeLanzarExcepcionSiEsMenorA1() {
        assertThrows(IllegalArgumentException.class, () -> new Calificacion(0));
    }

    @Test
    void debeLanzarExcepcionSiEsMayorA5() {
        assertThrows(IllegalArgumentException.class, () -> new Calificacion(6));
    }
}
