package com.openlib.market.domain.ventavendedor;

import java.util.Optional;

public interface IDetalleLibroGateway {
    Optional<String> obtenerIdVendedorPorIsbn(String isbn);
}
