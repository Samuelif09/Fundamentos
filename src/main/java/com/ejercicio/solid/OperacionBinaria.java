package com.ejercicio.solid;

public abstract class OperacionBinaria {
    
    // Contrato LSP: Define si los parametros son aceptables matemáticamente
    public boolean validarPrecondicion(int a, int b) {
        return true; 
    }
    
    public abstract double ejecutar(int a, int b);
}
