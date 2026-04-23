package com.ejercicio.solid;

/**
 * Principio ISP: Interfaz validable unaria, totalmente desvinculada de la ejecución matemática.
 */
public interface ValidableUnario {
    boolean validarPrecondicion(int a);
}
