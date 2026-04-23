package com.ejercicio.solid;

// Comprometemos nuestra clase abstracta solo con lo que usarán aquellos
// que necesiten un flujo interactivo. Si otro cliente solo quiere ejecutar
// puede usar EjecutableUnario unicamente (Respetando ISP).
public abstract class OperacionUnaria implements EjecutableUnario, ValidableUnario {
    
    @Override
    public boolean validarPrecondicion(int a) {
        return true; 
    }

    @Override
    public abstract double ejecutar(int a);
}
