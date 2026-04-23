package com.ejercicio.solid;

public abstract class OperacionUnaria {
    
    // Contrato LSP: Define si el parametro es aceptable matemáticamente
    public boolean validarPrecondicion(int a) {
        return true; 
    }

    public abstract double ejecutar(int a);
}
