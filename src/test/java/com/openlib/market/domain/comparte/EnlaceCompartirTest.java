package com.openlib.market.domain.comparte;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnlaceCompartirTest {

    @Test
    void debeGenerarUrlConFormatoCorrecto() {
        EnlaceCompartir enlace = new EnlaceCompartir("978-3-16-148410-0");
        String urlEsperada = "https://openlib.market/libros/978-3-16-148410-0?utm_source=share";
        
        assertEquals(urlEsperada, enlace.getUrl());
    }

    @Test
    void debeLanzarExcepcionSiIsbnEsInvalido() {
        assertThrows(IllegalArgumentException.class, () -> new EnlaceCompartir(null));
        assertThrows(IllegalArgumentException.class, () -> new EnlaceCompartir(""));
        assertThrows(IllegalArgumentException.class, () -> new EnlaceCompartir(" "));
    }
}
