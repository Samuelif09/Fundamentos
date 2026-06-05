package com.openlib.market.application.wishlist;

import com.openlib.market.domain.wishlist.IListaDeseosGateway;
import com.openlib.market.domain.wishlist.ListaDeseos;

import java.util.Collections;

public class VerWishlistInteractor {

    private final IListaDeseosGateway listaDeseosGateway;

    public VerWishlistInteractor(IListaDeseosGateway listaDeseosGateway) {
        this.listaDeseosGateway = listaDeseosGateway;
    }

    public WishlistResponseDto ejecutar(String idUsuario) {
        return listaDeseosGateway.obtenerPorUsuario(idUsuario)
                .map(lista -> new WishlistResponseDto(lista.getIdUsuario(), lista.getIsbns()))
                .orElse(new WishlistResponseDto(idUsuario, Collections.emptySet()));
    }
}
