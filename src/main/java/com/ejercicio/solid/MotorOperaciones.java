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
            return operacionesBinarias.get(opcion).ejecutar(a, b);
        } else if (operacionesUnarias.containsKey(opcion)) {
            return operacionesUnarias.get(opcion).ejecutar(a); // Operación unaria ignora 'b'
        }
        
        throw new IllegalArgumentException("Opcion invalida: " + opcion);
    }
}
