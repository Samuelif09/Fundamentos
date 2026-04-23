package com.ejercicio;

/**
 * Calculadora que respeta los principios SoLiD, estructurada completamente
 * dentro de una sola clase contenedora principal para cumplir con la restricción del ejercicio.
 */
public class CalculadoraSolid {

    /* ==========================================================
     *  INTERFACES (Principio 'I' - Interface Segregation)
     *  Separamos los tipos de operación para que los clientes
     *  solo dependan de lo que usan.
     * ========================================================== */

    public interface OperacionBinaria {
        double ejecutar(int a, int b);
    }

    public interface OperacionUnaria {
        double ejecutar(int a);
    }

    /* ==========================================================
     *  OPERACIONES (Principio 'O' - Open/Closed, 'S' - Single Responsibility, 'L' - Liskov)
     *  Cada implementación tiene un único propósito, podemos agregar
     *  las operaciones que necesitemos a futuro sin alterar otras,
     *  y cumplen con los contratos de las interfaces.
     * ========================================================== */

    public static class Suma implements OperacionBinaria {
        @Override
        public double ejecutar(int a, int b) {
            return (double) a + b;
        }
    }

    public static class Resta implements OperacionBinaria {
        @Override
        public double ejecutar(int a, int b) {
            return (double) a - b;
        }
    }

    public static class RaizCuadrada implements OperacionUnaria {
        @Override
        public double ejecutar(int a) {
            if (a < 0) {
                throw new IllegalArgumentException("No se puede calcular la raiz cuadrada de un numero negativo.");
            }
            return Math.sqrt(a);
        }
    }

    public static class LogaritmoNatural implements OperacionUnaria {
        @Override
        public double ejecutar(int a) {
            if (a <= 0) {
                throw new IllegalArgumentException("Logaritmo natural no definido para valores nulos o negativos.");
            }
            return Math.log(a);
        }
    }

    /* ==========================================================
     *  CONTROLADOR BASE (Principio 'D' - Dependency Inversion)
     *  La Calculadora base depende de las abstracciones (operaciones)
     *  creadas arriba y no de un tipo particular estancado.
     * ========================================================== */

    public static class Calculadora {
        // Ejecución para binarios usando la interfaz correspondiente
        public double calcular(OperacionBinaria operacion, int a, int b) {
            return operacion.ejecutar(a, b);
        }

        // Ejecución para unarios usando la interfaz separada
        public double calcular(OperacionUnaria operacion, int a) {
            return operacion.ejecutar(a);
        }
    }

    /* ==========================================================
     *  MÉTODO PRINCIPAL - APLICACIÓN Consola
     * ========================================================== */
    public static void main(String[] args) {
        System.out.println("=== Calculadora SoLiD en Consola ===\n");
        Calculadora calculadora = new Calculadora();

        // 1. Ejecutar Binarias
        OperacionBinaria operacionSuma = new Suma();
        double restSuma = calculadora.calcular(operacionSuma, 10, 5);
        System.out.println("Suma de 10 y 5: " + restSuma);

        OperacionBinaria operacionResta = new Resta();
        double restResta = calculadora.calcular(operacionResta, 10, 5);
        System.out.println("Resta de 10 y 5: " + restResta);

        // 2. Ejecutar Unarias
        OperacionUnaria operacionRaiz = new RaizCuadrada();
        double restRaiz = calculadora.calcular(operacionRaiz, 16);
        System.out.println("Raiz Cuadrada de 16: " + restRaiz);

        OperacionUnaria operacionLog = new LogaritmoNatural();
        double restLog = calculadora.calcular(operacionLog, 14); // ej: ln de 14
        System.out.println("Logaritmo Natural de 14: " + restLog);
        
        System.out.println("\n--- Fin de Ejecucion ---");
    }
}
