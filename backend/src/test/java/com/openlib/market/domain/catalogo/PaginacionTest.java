package com.openlib.market.domain.catalogo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaginacionTest {

    @Test
    void debeLanzarExcepcionSiPaginaEsNegativa() {
        assertThrows(IllegalArgumentException.class, () -> new Paginacion(-1, 20));
    }

    @Test
    void debeLanzarExcepcionSiTamanoEsCeroOMenor() {
        assertThrows(IllegalArgumentException.class, () -> new Paginacion(0, 0));
        assertThrows(IllegalArgumentException.class, () -> new Paginacion(0, -5));
    }

    @Test
    void debeLanzarExcepcionSiTamanoExcedeLimite() {
        assertThrows(IllegalArgumentException.class, () -> new Paginacion(0, 101));
    }

    @Test
    void debeCrearPaginacionValida() {
        Paginacion p = new Paginacion(2, 50);
        assertEquals(2, p.getPaginaActual());
        assertEquals(50, p.getTamanoPagina());
    }
}
