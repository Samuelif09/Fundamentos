package com.openlib.market.domain.detalle;

import java.util.Optional;

public interface IDetalleGateway {
    Optional<Libro> buscarPorId(Isbn isbn);
}
