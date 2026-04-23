package com.ejercicio.solid;

public class RaizCuadrada extends OperacionUnaria {
    
    @Override
    public boolean validarPrecondicion(int a) {
        return a >= 0; // Solo definimos el dominio. Para la raiz debe ser mayor o igual a 0.
    }

    @Override
    public double ejecutar(int a) {
        if (!validarPrecondicion(a)) {
            throw new IllegalArgumentException("No se puede calcular raiz de un negativo");
        }
        return Math.sqrt(a);
    }
}
