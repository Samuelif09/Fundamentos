package com.openlib.market.domain.soporte;

import java.util.Optional;

public interface IDisputaGateway {
    Optional<Disputa> buscarPorId(String id);
    void guardar(Disputa disputa);
}
