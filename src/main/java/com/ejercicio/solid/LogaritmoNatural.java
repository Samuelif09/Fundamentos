package com.ejercicio.solid;

public class LogaritmoNatural extends OperacionUnaria {
    @Override
    public double ejecutar(int a) {
        if (a <= 0) {
            throw new IllegalArgumentException("No se puede calcular logaritmo de cero o negativo");
        }
        return Math.log(a);
    }
}
