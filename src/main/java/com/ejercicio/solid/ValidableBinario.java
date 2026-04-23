package com.ejercicio.solid;

/**
 * Principio ISP: Interfaz separada exclusivamente para validar restricciones lógicas.
 * Ninguna clase que solo requiera calcular debería ser obligada a forzar esta validación
 * si no la utiliza de manera directa.
 */
public interface ValidableBinario {
    boolean validarPrecondicion(int a, int b);
}
