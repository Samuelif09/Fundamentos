package com.openlib.market.domain.busqueda;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PalabraClaveTest {

    @Test
    void debeCrearPalabraClaveSiEsValida() {
        PalabraClave palabra = new PalabraClave("java spring");
        assertEquals("java spring", palabra.getValor());
    }

    @Test
    void debeLanzarExcepcionSiEsNula() {
        assertThrows(IllegalArgumentException.class, () -> new PalabraClave(null));
    }

    @Test
    void debeLanzarExcepcionSiEsCorta() {
        assertThrows(IllegalArgumentException.class, () -> new PalabraClave("a"));
        assertThrows(IllegalArgumentException.class, () -> new PalabraClave("ab"));
    }

    @Test
    void debeLanzarExcepcionSiTieneCaracteresPeligrosos() {
        assertThrows(IllegalArgumentException.class, () -> new PalabraClave("java; drop table"));
        assertThrows(IllegalArgumentException.class, () -> new PalabraClave("java%20--"));
    }
}
