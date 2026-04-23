package com.ejercicio.solid;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OperacionesTest {

    @Test
    void deberiaSumarDosNumeros() {
        OperacionBinaria suma = new Suma();
        assertEquals(15.0, suma.ejecutar(10, 5));
    }

    @Test
    void deberiaRestarDosNumeros() {
        OperacionBinaria resta = new Resta();
        assertEquals(5.0, resta.ejecutar(10, 5));
    }

    @Test
    void deberiaCalcularRaizCuadradaPositiva() {
        OperacionUnaria raiz = new RaizCuadrada();
        assertEquals(4.0, raiz.ejecutar(16));
    }

    @Test
    void deberiaFallarRaizCuadradaNegativa() {
        OperacionUnaria raiz = new RaizCuadrada();
        Exception e = assertThrows(IllegalArgumentException.class, () -> raiz.ejecutar(-4));
        assertEquals("No se puede calcular raiz de un negativo", e.getMessage());
    }

    @Test
    void deberiaCalcularLogaritmoNatural() {
        OperacionUnaria log = new LogaritmoNatural();
        assertEquals(0.0, log.ejecutar(1));
    }

    @Test
    void deberiaFallarLogaritmoCeroONegativo() {
        OperacionUnaria log = new LogaritmoNatural();
        assertThrows(IllegalArgumentException.class, () -> log.ejecutar(0));
    }
}
