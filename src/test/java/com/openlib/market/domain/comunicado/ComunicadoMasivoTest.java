package com.openlib.market.domain.comunicado;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ComunicadoMasivoTest {

    @Test
    void debeLanzarExcepcionSiAsuntoEsVacio() {
        assertThrows(IllegalArgumentException.class, () -> 
            new ComunicadoMasivo("", "Cuerpo", FiltroDestinatarios.TODOS)
        );
    }

    @Test
    void debeLanzarExcepcionSiCuerpoEsVacio() {
        assertThrows(IllegalArgumentException.class, () -> 
            new ComunicadoMasivo("Asunto", "   ", FiltroDestinatarios.TODOS)
        );
    }

    @Test
    void debeRegistrarCantidadDeEnvios() {
        ComunicadoMasivo comunicado = new ComunicadoMasivo("Actualización", "Cuerpo del mensaje", FiltroDestinatarios.SOLO_VENDEDORES);
        comunicado.registrarEnvio(150);
        assertEquals(150, comunicado.getCantidadDestinatarios());
    }
}
