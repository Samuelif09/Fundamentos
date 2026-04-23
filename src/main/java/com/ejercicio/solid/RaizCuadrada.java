package com.ejercicio.solid;

public class RaizCuadrada extends OperacionUnaria {
    @Override
    public double ejecutar(int a) {
        if (a < 0) {
            throw new IllegalArgumentException("No se puede calcular raiz de un negativo");
        }
        return Math.sqrt(a);
    }
}
