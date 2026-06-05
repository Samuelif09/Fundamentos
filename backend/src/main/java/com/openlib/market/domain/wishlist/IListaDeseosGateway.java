package com.openlib.market.domain.wishlist;

import java.util.Optional;

public interface IListaDeseosGateway {
    Optional<ListaDeseos> obtenerPorUsuario(String idUsuario);
    void guardar(ListaDeseos lista);
}
