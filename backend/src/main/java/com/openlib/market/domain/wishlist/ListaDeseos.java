package com.openlib.market.domain.wishlist;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ListaDeseos {
    private final String idUsuario;
    private final Set<String> isbns;

    public ListaDeseos(String idUsuario) {
        this.idUsuario = idUsuario;
        this.isbns = new HashSet<>();
    }

    public ListaDeseos(String idUsuario, Set<String> isbns) {
        this.idUsuario = idUsuario;
        this.isbns = new HashSet<>(isbns); // Defensive copy
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public Set<String> getIsbns() {
        return Collections.unmodifiableSet(isbns);
    }

    public void agregarItem(String isbn) {
        if (isbn != null && !isbn.trim().isEmpty()) {
            this.isbns.add(isbn);
        }
    }

    public void removerItem(String isbn) {
        if (isbn != null) {
            this.isbns.remove(isbn);
        }
    }
}
