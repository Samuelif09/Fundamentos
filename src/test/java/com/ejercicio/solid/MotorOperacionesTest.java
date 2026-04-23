package com.ejercicio.solid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MotorOperacionesTest {

    private MotorOperaciones motor;

    @BeforeEach
    void setUp() {
        motor = new MotorOperaciones();
        motor.registrarOperacionBinaria(1, new Suma());
        motor.registrarOperacionUnaria(3, new RaizCuadrada());
    }

    @Test
    void deberiaLlamarSumaCuandoOpcionEs1() {
        assertEquals(15.0, motor.procesar(1, 10, 5));
    }

    @Test
    void deberiaLlamarRaizCuandoOpcionEs3() {
        assertEquals(4.0, motor.procesar(3, 16, 0)); 
    }

    @Test
    void deberiaLanzarExcepcionConOpcionInvalida() {
        assertThrows(IllegalArgumentException.class, () -> motor.procesar(99, 1, 1));
    }

    @Test
    void deberiaLanzarExcepcionPidiendoAlMotorAlNoPasarPrecondicionLSP() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> motor.procesar(3, -9, 0));
        assertEquals("Valor no valido para esta operacion unaria.", e.getMessage());
    }
}
