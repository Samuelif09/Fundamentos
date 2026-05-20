package com.openlib.market.domain.catalogo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CriterioBusquedaTest {

    @Test
    void debeCrearCriterioConAmbosParametros() {
        CriterioBusqueda cb = new CriterioBusqueda("Harry", "Rowling", null);
        assertTrue(cb.tieneTitulo());
        assertTrue(cb.tieneAutor());
        assertEquals("Harry", cb.getTitulo());
        assertEquals("Rowling", cb.getAutor());
    }

    @Test
    void debeCrearCriterioSoloConTitulo() {
        CriterioBusqueda cb = new CriterioBusqueda(" El Hobbit ", null, null);
        assertTrue(cb.tieneTitulo());
        assertEquals("El Hobbit", cb.getTitulo());
        assertFalse(cb.tieneAutor());
        assertFalse(cb.tieneCategoria());
    }

    @Test
    void debeCrearCriterioSoloConAutor() {
        CriterioBusqueda cb = new CriterioBusqueda("  ", "Rowling ", null);
        assertFalse(cb.tieneTitulo());
        assertTrue(cb.tieneAutor());
        assertEquals("Rowling", cb.getAutor());
        assertFalse(cb.tieneCategoria());
    }

    @Test
    void debeLanzarExcepcionSiAmbosSonVacios() {
        assertThrows(IllegalArgumentException.class, () -> new CriterioBusqueda(null, null, null));
        assertThrows(IllegalArgumentException.class, () -> new CriterioBusqueda("   ", "  ", ""));
    }

    @Test
    void debeCrearseSiAlMenosUnoEsValido() {
        assertDoesNotThrow(() -> new CriterioBusqueda("Clean Code", null, null));
        assertDoesNotThrow(() -> new CriterioBusqueda(null, "Martin", null));
        assertDoesNotThrow(() -> new CriterioBusqueda(null, null, "PROGRAMACION"));
    }
}
