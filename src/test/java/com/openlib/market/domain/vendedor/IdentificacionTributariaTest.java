package com.openlib.market.domain.vendedor;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IdentificacionTributariaTest {

    @Test
    void debeCrearIdentificacionValida() {
        IdentificacionTributaria id = new IdentificacionTributaria("NIT-12345678");
        assertEquals("NIT-12345678", id.getValor());
    }

    @Test
    void debeLanzarExcepcionSiEstaVacia() {
        assertThrows(IllegalArgumentException.class, () -> new IdentificacionTributaria(""));
        assertThrows(IllegalArgumentException.class, () -> new IdentificacionTributaria(null));
        assertThrows(IllegalArgumentException.class, () -> new IdentificacionTributaria("   "));
    }

    @Test
    void debeLanzarExcepcionSiTieneCaracteresInvalidosOLongitudIncorrecta() {
        assertThrows(IllegalArgumentException.class, () -> new IdentificacionTributaria("1234")); // muy corta
        assertThrows(IllegalArgumentException.class, () -> new IdentificacionTributaria("NIT@1234")); // arroba no permitida
        assertThrows(IllegalArgumentException.class, () -> new IdentificacionTributaria("123456789012345678901")); // muy larga
    }
}
