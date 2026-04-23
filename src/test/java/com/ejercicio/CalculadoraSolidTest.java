package com.ejercicio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculadoraSolidTest {

    private final CalculadoraSolid.Calculadora calculadora = new CalculadoraSolid.Calculadora();

    @Test
    @DisplayName("Prueba de Suma - Binaria")
    void testSuma() {
        CalculadoraSolid.OperacionBinaria suma = new CalculadoraSolid.Suma();
        assertEquals(15.0, calculadora.calcular(suma, 10, 5));
    }

    @Test
    @DisplayName("Prueba de Resta - Binaria")
    void testResta() {
        CalculadoraSolid.OperacionBinaria resta = new CalculadoraSolid.Resta();
        assertEquals(5.0, calculadora.calcular(resta, 10, 5));
    }

    @Test
    @DisplayName("Prueba de Raiz Cuadrada - Unaria y Positiva")
    void testRaizCuadradaPositivo() {
        CalculadoraSolid.OperacionUnaria raiz = new CalculadoraSolid.RaizCuadrada();
        assertEquals(4.0, calculadora.calcular(raiz, 16));
    }

    @Test
    @DisplayName("Prueba de Raiz Cuadrada - Error al ser Negativo")
    void testRaizCuadradaNegativoError() {
        CalculadoraSolid.OperacionUnaria raiz = new CalculadoraSolid.RaizCuadrada();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> calculadora.calcular(raiz, -1));
        assertTrue(exception.getMessage().contains("negativo"));
    }

    @Test
    @DisplayName("Prueba de Logaritmo Natural - Positivo")
    void testLogaritmoNatural() {
        CalculadoraSolid.OperacionUnaria logaritmo = new CalculadoraSolid.LogaritmoNatural();
        // Math.log(1) es 0.0
        assertEquals(0.0, calculadora.calcular(logaritmo, 1));
    }
    
    @Test
    @DisplayName("Prueba de Logaritmo Natural - Error al ser Negativo o Cero")
    void testLogaritmoNaturalNegativoError() {
        CalculadoraSolid.OperacionUnaria logaritmo = new CalculadoraSolid.LogaritmoNatural();
        assertThrows(IllegalArgumentException.class, () -> calculadora.calcular(logaritmo, 0));
        assertThrows(IllegalArgumentException.class, () -> calculadora.calcular(logaritmo, -5));
    }
}
