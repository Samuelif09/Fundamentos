package com.ejercicio.solid;

public class MotorOperaciones {

    private final Operaciones operaciones;

    public MotorOperaciones(Operaciones operaciones) {
        this.operaciones = operaciones;
    }

    public double procesar(int opcion, int a, int b) {
        switch (opcion) {
            case 1:
                return operaciones.sumar(a, b);
            case 2:
                return operaciones.restar(a, b);
            case 3:
                // Para las operaciones unarias como la raíz, usamos solo 'a'
                return operaciones.raiz(a);
            case 4:
                return operaciones.logaritmo(a);
            default:
                throw new IllegalArgumentException("Opcion invalida");
        }
    }
}
