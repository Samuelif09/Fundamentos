package com.openlib.market.domain.afiliado;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProgramaAfiliadoTest {

    @Test
    void debeLanzarExcepcionSiComisionExcede100() {
        // La plataforma retiene 15%. Si le damos 90% al afiliado, la suma es 105%.
        PorcentajeComisionAfiliado comisionAlta = new PorcentajeComisionAfiliado(90.0);

        assertThrows(ComisionInvalidaException.class, () -> {
            new ProgramaAfiliado("vendedor123", comisionAlta);
        });
    }

    @Test
    void noDebeLanzarExcepcionSiComisionEsValida() {
        // La plataforma retiene 15%. Si le damos 60% al afiliado, la suma es 75% (válido).
        PorcentajeComisionAfiliado comisionValida = new PorcentajeComisionAfiliado(60.0);
        new ProgramaAfiliado("vendedor123", comisionValida);
    }
}
