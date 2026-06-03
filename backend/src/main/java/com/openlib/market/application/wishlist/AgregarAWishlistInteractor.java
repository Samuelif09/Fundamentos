package com.openlib.market.application.wishlist;

import com.openlib.market.domain.wishlist.IListaDeseosGateway;
import com.openlib.market.domain.wishlist.ListaDeseos;

public class AgregarAWishlistInteractor {

    private final IListaDeseosGateway listaDeseosGateway;

    public AgregarAWishlistInteractor(IListaDeseosGateway listaDeseosGateway) {
        this.listaDeseosGateway = listaDeseosGateway;
    }

    public void ejecutar(String idUsuario, String isbn) {
        ListaDeseos lista = listaDeseosGateway.obtenerPorUsuario(idUsuario)
                .orElse(new ListaDeseos(idUsuario));
        
        lista.agregarItem(isbn);
        listaDeseosGateway.guardar(lista);
    }
}
