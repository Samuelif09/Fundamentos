package com.ejercicio.solid;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OperacionesTest {

    private final Operaciones operaciones = new Operaciones();

    @Test
    void deberiaSumarDosNumeros() {
        assertEquals(15.0, operaciones.sumar(10, 5));
    }

    @Test
    void deberiaRestarDosNumeros() {
        assertEquals(5.0, operaciones.restar(10, 5));
    }

    @Test
    void deberiaCalcularRaizCuadradaPositiva() {
        assertEquals(4.0, operaciones.raiz(16));
    }

    @Test
    void deberiaFallarRaizCuadradaNegativa() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> operaciones.raiz(-4));
        assertEquals("No se puede calcular raiz de un negativo", e.getMessage());
    }

    @Test
    void deberiaCalcularLogaritmoNatural() {
        assertEquals(0.0, operaciones.logaritmo(1));
    }

    @Test
    void deberiaFallarLogaritmoCeroONegativo() {
        assertThrows(IllegalArgumentException.class, () -> operaciones.logaritmo(0));
    }
}
