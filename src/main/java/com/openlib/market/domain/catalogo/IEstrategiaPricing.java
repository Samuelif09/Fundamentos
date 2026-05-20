package com.openlib.market.domain.catalogo;

import java.util.List;

public interface IEstrategiaPricing {
    double calcularNuevoPrecio(double precioActual, List<Double> preciosCompetencia, PrecioMinimo minimo, PrecioMaximo maximo);
}
