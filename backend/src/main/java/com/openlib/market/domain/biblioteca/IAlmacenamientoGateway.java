package com.openlib.market.domain.biblioteca;

import java.util.Optional;

public interface IAlmacenamientoGateway {
    Optional<ArchivoDigital> recuperarArchivo(String idLibro);
}
