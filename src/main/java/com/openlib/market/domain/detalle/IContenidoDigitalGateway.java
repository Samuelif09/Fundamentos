package com.openlib.market.domain.detalle;

import java.util.Optional;

public interface IContenidoDigitalGateway {
    void guardarContenido(ContenidoDigital contenido);
    Optional<ContenidoDigital> obtenerContenidoPorId(String id);
}
