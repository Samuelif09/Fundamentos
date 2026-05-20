package com.openlib.market.domain.catalogo;

import java.util.List;

public class PromedioStrategy implements IEstrategiaPricing {

    @Override
    public double calcularNuevoPrecio(double precioActual, List<Double> preciosCompetencia, PrecioMinimo minimo, PrecioMaximo maximo) {
        if (preciosCompetencia == null || preciosCompetencia.isEmpty()) {
            return precioActual;
        }

        double promedio = preciosCompetencia.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(precioActual);

        // Estrategia: Por debajo del promedio (por ejemplo 5% menos que el promedio)
        double nuevoPrecio = promedio * 0.95;

        if (nuevoPrecio < minimo.getValor()) {
            return minimo.getValor();
        }
        if (nuevoPrecio > maximo.getValor()) {
            return maximo.getValor();
        }

        return nuevoPrecio;
    }
}
