package com.ejercicio.solid;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MotorOperacionesTest {

    @Test
    void deberiaLlamarSumaCuandoOpcionEs1() {
        Operaciones op = new Operaciones();
        MotorOperaciones motor = new MotorOperaciones(op);
        
        // Asumiendo Opcion 1 := Suma
        assertEquals(15.0, motor.procesar(1, 10, 5));
    }

    @Test
    void deberiaLlamarRaizCuandoOpcionEs3() {
        Operaciones op = new Operaciones();
        MotorOperaciones motor = new MotorOperaciones(op);
        
        // Asumiendo Opcion 3 := Raiz Cuadrada (usa solo el primer operador)
        assertEquals(4.0, motor.procesar(3, 16, 0)); 
    }

    @Test
    void deberiaLanzarExcepcionConOpcionInvalida() {
        Operaciones op = new Operaciones();
        MotorOperaciones motor = new MotorOperaciones(op);
        
        assertThrows(IllegalArgumentException.class, () -> motor.procesar(99, 1, 1));
    }
}
