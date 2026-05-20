package com.openlib.market.domain.curaduria;

import com.openlib.market.domain.detalle.EstadoLibro;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.Precio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CuraduriaDomainTest {

    @Test
    void debeLanzarExcepcionSiMotivoRechazoEsNuloOVacio() {
        assertThrows(IllegalArgumentException.class, () -> new MotivoRechazo(null));
        assertThrows(IllegalArgumentException.class, () -> new MotivoRechazo("   "));
    }

    @Test
    void debeLanzarExcepcionSiMotivoRechazoEsMuyCorto() {
        assertThrows(IllegalArgumentException.class, () -> new MotivoRechazo("Corta"));
    }

    @Test
    void debeCrearMotivoRechazoSiEsValido() {
        MotivoRechazo motivo = new MotivoRechazo("El libro contiene material inapropiado.");
        assertEquals("El libro contiene material inapropiado.", motivo.getRazon());
    }

    @Test
    void debeRechazarLibroCorrectamente() {
        Libro libro = new Libro(
                new Isbn("978-3-16-148410-0"),
                "Titulo",
                "Sinopsis",
                new Precio(10.0),
                "url",
                "cat",
                "vendedor1",
                EstadoLibro.EN_REVISION,
                null
        );

        Libro rechazado = libro.rechazar(new MotivoRechazo("Falta calidad en portada."));
        assertEquals(EstadoLibro.RECHAZADO, rechazado.getEstado());
    }

    @Test
    void debeLanzarExcepcionAlRechazarLibroYaRechazado() {
        Libro libro = new Libro(
                new Isbn("978-3-16-148410-0"),
                "Titulo",
                "Sinopsis",
                new Precio(10.0),
                "url",
                "cat",
                "vendedor1",
                EstadoLibro.RECHAZADO,
                null
        );

        assertThrows(IllegalStateException.class, () -> libro.rechazar(new MotivoRechazo("Motivo valido aqui")));
    }
}
