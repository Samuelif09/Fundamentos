package com.openlib.market.domain.configuracion;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConfiguracionDomainTest {

    @Test
    void debeDeshabilitarSiHayMasDeUnoActivo() {
        ConfiguracionMetodoPago config = new ConfiguracionMetodoPago(new NombreMetodo("Stripe"), EstadoMetodoPago.HABILITADO);
        config.deshabilitar(2);
        assertEquals(EstadoMetodoPago.DESHABILITADO, config.getEstado());
    }

    @Test
    void noDebeDeshabilitarSiEsElUltimoActivo() {
        ConfiguracionMetodoPago config = new ConfiguracionMetodoPago(new NombreMetodo("Stripe"), EstadoMetodoPago.HABILITADO);
        assertThrows(ConfiguracionInvalidaException.class, () -> config.deshabilitar(1));
    }

    @Test
    void debeHabilitarCorrectamente() {
        ConfiguracionMetodoPago config = new ConfiguracionMetodoPago(new NombreMetodo("PayPal"), EstadoMetodoPago.DESHABILITADO);
        config.habilitar();
        assertEquals(EstadoMetodoPago.HABILITADO, config.getEstado());
    }

    @Test
    void nombreMetodoNoPuedeSerVacio() {
        assertThrows(IllegalArgumentException.class, () -> new NombreMetodo(""));
        assertThrows(IllegalArgumentException.class, () -> new NombreMetodo(null));
    }
}
