package com.ejercicio.solid;

import java.util.HashMap;
import java.util.Map;

public class MotorOperaciones {
    private final Map<Integer, OperacionBinaria> operacionesBinarias = new HashMap<>();
    private final Map<Integer, OperacionUnaria> operacionesUnarias = new HashMap<>();

    public void registrarOperacionBinaria(int opcion, OperacionBinaria operacion) {
        operacionesBinarias.put(opcion, operacion);
    }

    public void registrarOperacionUnaria(int opcion, OperacionUnaria operacion) {
        operacionesUnarias.put(opcion, operacion);
    }

    public double procesar(int opcion, int a, int b) {
        if (operacionesBinarias.containsKey(opcion)) {
            OperacionBinaria op = operacionesBinarias.get(opcion);
            if (!op.validarPrecondicion(a, b)) {
                throw new IllegalArgumentException("Valores no validos para esta operacion binaria.");
            }
            return op.ejecutar(a, b);
            
        } else if (operacionesUnarias.containsKey(opcion)) {
            OperacionUnaria op = operacionesUnarias.get(opcion);
            if (!op.validarPrecondicion(a)) {
                throw new IllegalArgumentException("Valor no valido para esta operacion unaria.");
            }
            return op.ejecutar(a); // Operación unaria ignora 'b'
        }
        
        throw new IllegalArgumentException("Opcion invalida: " + opcion);
    }
}
