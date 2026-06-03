package com.openlib.market.application.wishlist;

import com.openlib.market.domain.wishlist.IListaDeseosGateway;
import com.openlib.market.domain.wishlist.ListaDeseos;

public class RemoverDeWishlistInteractor {

    private final IListaDeseosGateway listaDeseosGateway;

    public RemoverDeWishlistInteractor(IListaDeseosGateway listaDeseosGateway) {
        this.listaDeseosGateway = listaDeseosGateway;
    }

    public void ejecutar(String idUsuario, String isbn) {
        listaDeseosGateway.obtenerPorUsuario(idUsuario).ifPresent(lista -> {
            lista.removerItem(isbn);
            listaDeseosGateway.guardar(lista);
        });
    }
}
