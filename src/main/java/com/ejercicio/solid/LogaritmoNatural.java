package com.ejercicio.solid;

public class LogaritmoNatural extends OperacionUnaria {

    @Override
    public boolean validarPrecondicion(int a) {
        return a > 0; // Para el logaritmo natural debe ser estrictamente mayor a 0
    }

    @Override
    public double ejecutar(int a) {
        if (!validarPrecondicion(a)) {
            throw new IllegalArgumentException("No se puede calcular logaritmo de cero o negativo");
        }
        return Math.log(a);
    }
}
