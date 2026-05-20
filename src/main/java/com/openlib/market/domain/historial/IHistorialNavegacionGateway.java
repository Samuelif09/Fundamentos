package com.openlib.market.domain.historial;

import java.util.Optional;

public interface IHistorialNavegacionGateway {
    Optional<HistorialNavegacion> obtenerPorUsuario(String idUsuario);
    void guardar(HistorialNavegacion historial);
}
