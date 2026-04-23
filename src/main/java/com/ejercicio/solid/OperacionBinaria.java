package com.ejercicio.solid;

// Cumplimos con ISP: en vez de tener una super-interfaz obligando a todo,
// ensamblamos pequeños bloques bajo demanda.
public abstract class OperacionBinaria implements EjecutableBinario, ValidableBinario {
    
    // Contrato LSP: Define si los parametros son aceptables matemáticamente
    @Override
    public boolean validarPrecondicion(int a, int b) {
        return true; 
    }
    
    @Override
    public abstract double ejecutar(int a, int b);
}
