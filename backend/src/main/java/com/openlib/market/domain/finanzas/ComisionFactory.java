package com.openlib.market.domain.finanzas;
public class ComisionFactory {
    public IComisionStrategy obtenerEstrategia(String tipoProductoStr) {
        if (tipoProductoStr == null) return new ComisionDigitalStrategy();
        String upper = tipoProductoStr.toUpperCase();
        if (upper.equals("LIBRO")) {
            return new ComisionFisicaStrategy();
        }
        return new ComisionDigitalStrategy();
    }
}
