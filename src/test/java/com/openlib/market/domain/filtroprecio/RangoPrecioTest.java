package com.openlib.market.domain.filtroprecio;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RangoPrecioTest {

    @Test
    void debeCrearRangoValido() {
        RangoPrecio rango = new RangoPrecio(10.0, 50.0);
        assertEquals(10.0, rango.getMin());
        assertEquals(50.0, rango.getMax());
    }

    @Test
    void debeLanzarExcepcionSiMinEsNegativo() {
        assertThrows(IllegalArgumentException.class, () -> new RangoPrecio(-1.0, 10.0));
    }

    @Test
    void debeLanzarExcepcionSiMaxEsMenorQueMin() {
        assertThrows(IllegalArgumentException.class, () -> new RangoPrecio(50.0, 10.0));
    }

    @Test
    void debeValidarSiPrecioEstaEnRango() {
        RangoPrecio rango = new RangoPrecio(10.0, 50.0);
        assertTrue(rango.estaDentroDelRango(25.0));
        assertTrue(rango.estaDentroDelRango(10.0));
        assertTrue(rango.estaDentroDelRango(50.0));
        assertFalse(rango.estaDentroDelRango(9.99));
        assertFalse(rango.estaDentroDelRango(50.01));
    }
}
