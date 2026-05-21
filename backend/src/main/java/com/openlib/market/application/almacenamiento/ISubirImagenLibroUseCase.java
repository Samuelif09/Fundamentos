package com.openlib.market.application.almacenamiento;

import com.openlib.market.domain.almacenamiento.ArchivoImagen;

public interface ISubirImagenLibroUseCase {
    String subirPortada(String idLibro, ArchivoImagen archivo);
}
