package com.ejercicio.solid;

public class Operaciones {

    public double sumar(int a, int b) {
        return (double) a + b;
    }

    public double restar(int a, int b) {
        return (double) a - b;
    }

    public double raiz(int a) {
        if (a < 0) {
            // Este mensaje concuerda con lo esperado en OperacionesTest.java
            throw new IllegalArgumentException("No se puede calcular raiz de un negativo");
        }
        return Math.sqrt(a);
    }

    public double logaritmo(int a) {
        if (a <= 0) {
            throw new IllegalArgumentException("No se puede calcular logaritmo de cero o negativo");
        }
        return Math.log(a);
    }
}
