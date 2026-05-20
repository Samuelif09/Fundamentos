package com.openlib.market.domain.catalogo;

import java.util.List;

public class IgualarPrecioStrategy implements IEstrategiaPricing {

    @Override
    public double calcularNuevoPrecio(double precioActual, List<Double> preciosCompetencia, PrecioMinimo minimo, PrecioMaximo maximo) {
        if (preciosCompetencia == null || preciosCompetencia.isEmpty()) {
            return precioActual;
        }

        double precioMasBajo = preciosCompetencia.stream()
                .mapToDouble(Double::doubleValue)
                .min()
                .orElse(precioActual);

        if (precioMasBajo < minimo.getValor()) {
            return minimo.getValor();
        }
        if (precioMasBajo > maximo.getValor()) {
            return maximo.getValor();
        }

        return precioMasBajo;
    }
}
