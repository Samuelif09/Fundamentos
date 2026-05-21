package com.openlib.market.domain.detalle;

import java.util.Optional;

public interface ILibroPublicacionGateway {
    void guardar(Libro libro);
    void actualizar(Libro libro);
    Optional<Libro> obtenerPorIsbn(String isbn);
}
