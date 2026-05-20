package com.openlib.market.application.biblioteca;

import com.openlib.market.domain.biblioteca.ArchivoDigital;

public interface IDescargarPostCompraUseCase {
    ArchivoDigital descargarLibro(String idUsuario, String idLibro);
}
