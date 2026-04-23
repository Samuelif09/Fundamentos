package com.ejercicio.solid;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MenuTest {

    @Test
    void deberiaMostrarOpcionesEnConsola() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        Menu menu = new Menu(printStream);

        // Act
        menu.mostrarOpciones();

        // Assert
        String salida = outputStream.toString();
        assertTrue(salida.contains("1. Sumar"));
        assertTrue(salida.contains("2. Restar"));
        assertTrue(salida.contains("3. Raiz Cuadrada"));
        assertTrue(salida.contains("4. Logaritmo Natural"));
    }
    
    @Test
    void deberiaMostrarResultado() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        Menu menu = new Menu(printStream);

        // Act
        menu.mostrarResultado(42.5);

        // Assert
        String salida = outputStream.toString();
        assertTrue(salida.contains("El resultado es: 42.5"));
    }
}
