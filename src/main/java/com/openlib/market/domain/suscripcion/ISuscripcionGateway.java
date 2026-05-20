package com.openlib.market.domain.suscripcion;

public interface ISuscripcionGateway {
    void guardar(SuscripcionAutor suscripcion);
    boolean existeSuscripcion(String idComprador, String idVendedor);
}
