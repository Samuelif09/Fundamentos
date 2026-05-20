package com.openlib.market.domain.categoria;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoriaDomainTest {

    @Test
    void debeNormalizarNombreDeCategoria() {
        NombreCategoria cat = new NombreCategoria("  ciencia   FICCIÓN  ");
        assertEquals("Ciencia ficción", cat.getValor());
        assertEquals("ciencia ficción", cat.getNormalizado());
    }

    @Test
    void dosNombresNormalizadosDebenSerIguales() {
        NombreCategoria cat1 = new NombreCategoria("Fantasía");
        NombreCategoria cat2 = new NombreCategoria(" fAntasÍa  ");
        assertEquals(cat1, cat2);
    }

    @Test
    void debeLanzarExcepcionSiNombreEsVacio() {
        assertThrows(IllegalArgumentException.class, () -> new NombreCategoria("   "));
        assertThrows(IllegalArgumentException.class, () -> new NombreCategoria(null));
    }

    @Test
    void debeCrearCategoriaConEstadoActivaPorDefecto() {
        CategoriaCatalogo cat = new CategoriaCatalogo(new NombreCategoria("Terror"));
        assertEquals(EstadoCategoria.ACTIVA, cat.getEstado());
    }

    @Test
    void debeDesactivarYActivarCategoria() {
        CategoriaCatalogo cat = new CategoriaCatalogo(new NombreCategoria("Romance"));
        cat.desactivar();
        assertEquals(EstadoCategoria.INACTIVA, cat.getEstado());
        cat.activar();
        assertEquals(EstadoCategoria.ACTIVA, cat.getEstado());
    }
}
