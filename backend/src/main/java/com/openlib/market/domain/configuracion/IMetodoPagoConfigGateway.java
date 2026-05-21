package com.openlib.market.domain.configuracion;

import java.util.List;
import java.util.Optional;

public interface IMetodoPagoConfigGateway {
    void actualizar(ConfiguracionMetodoPago configuracion);
    Optional<ConfiguracionMetodoPago> obtenerPorId(String id);
    List<ConfiguracionMetodoPago> listarTodos();
    int contarMetodosHabilitados();
}
