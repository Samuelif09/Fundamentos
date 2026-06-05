package com.openlib.market.application.wishlist;

import java.util.Set;

public class WishlistResponseDto {
    private String idUsuario;
    private Set<String> isbns;

    public WishlistResponseDto(String idUsuario, Set<String> isbns) {
        this.idUsuario = idUsuario;
        this.isbns = isbns;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public Set<String> getIsbns() {
        return isbns;
    }
}
