package com.openlib.market.domain.vendedor;

import java.util.Optional;

public interface IVendedorGateway {
    void guardar(Vendedor vendedor);
    void actualizar(Vendedor vendedor);
    boolean existePorIdentificacionTributaria(String identificacionTributaria);
    Optional<Vendedor> obtenerPorId(String id);
    Optional<Vendedor> obtenerPorIdUsuario(String idUsuario);
}
