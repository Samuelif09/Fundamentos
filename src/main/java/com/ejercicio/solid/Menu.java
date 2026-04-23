package com.ejercicio.solid;

import java.io.PrintStream;

public class Menu {

    private final PrintStream outputStream;

    // Inyectamos la dependencia de PrintStream para facilitar testearlo sin afectar la consola real.
    public Menu(PrintStream outputStream) {
        this.outputStream = outputStream;
    }

    public void mostrarOpciones() {
        outputStream.println("=== Calculadora SoLiD (SRP) ===");
        outputStream.println("1. Sumar");
        outputStream.println("2. Restar");
        outputStream.println("3. Raiz Cuadrada");
        outputStream.println("4. Logaritmo Natural");
        outputStream.println("0. Salir");
    }

    public void mostrarResultado(double resultado) {
        outputStream.println("El resultado es: " + resultado);
    }
}
