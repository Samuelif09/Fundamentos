package com.ejercicio.solid;

public class Resta extends OperacionBinaria {
    @Override
    public double ejecutar(int a, int b) {
        return (double) a - b;
    }
}
