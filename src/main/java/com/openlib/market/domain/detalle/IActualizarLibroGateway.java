package com.openlib.market.domain.detalle;

import java.util.Optional;

public interface IActualizarLibroGateway {
    Optional<Libro> buscarPorIsbn(Isbn isbn);
    void actualizar(Libro libro);
}
