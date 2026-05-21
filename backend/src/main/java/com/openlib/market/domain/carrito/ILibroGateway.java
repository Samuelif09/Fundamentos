package com.openlib.market.domain.carrito;

import java.util.Optional;

public interface ILibroGateway {
    Optional<LibroSnapshot> obtenerPorIsbn(String isbn);
}
