package com.openlib.market.domain.catalogo;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EstrategiaPricingTest {

    @Test
    void igualarMasBajoDebeRetornar10() {
        IgualarPrecioStrategy strategy = new IgualarPrecioStrategy();
        List<Double> preciosCompetencia = List.of(10.0, 12.0, 15.0);
        PrecioMinimo minimo = new PrecioMinimo(9.0);
        PrecioMaximo maximo = new PrecioMaximo(20.0);

        double nuevoPrecio = strategy.calcularNuevoPrecio(15.0, preciosCompetencia, minimo, maximo);

        assertEquals(10.0, nuevoPrecio);
    }

    @Test
    void igualarMasBajoDebeRespetarPrecioMinimo() {
        IgualarPrecioStrategy strategy = new IgualarPrecioStrategy();
        List<Double> preciosCompetencia = List.of(8.0, 12.0, 15.0);
        PrecioMinimo minimo = new PrecioMinimo(9.0); // No puedo bajar a 8
        PrecioMaximo maximo = new PrecioMaximo(20.0);

        double nuevoPrecio = strategy.calcularNuevoPrecio(15.0, preciosCompetencia, minimo, maximo);

        assertEquals(9.0, nuevoPrecio);
    }

    @Test
    void promedioStrategyDebeRetornar5PorCientoDebajoDelPromedio() {
        PromedioStrategy strategy = new PromedioStrategy();
        List<Double> preciosCompetencia = List.of(10.0, 20.0, 30.0); // Promedio es 20
        PrecioMinimo minimo = new PrecioMinimo(10.0);
        PrecioMaximo maximo = new PrecioMaximo(50.0);

        double nuevoPrecio = strategy.calcularNuevoPrecio(25.0, preciosCompetencia, minimo, maximo);

        // 20 * 0.95 = 19.0
        assertEquals(19.0, nuevoPrecio);
    }
}
