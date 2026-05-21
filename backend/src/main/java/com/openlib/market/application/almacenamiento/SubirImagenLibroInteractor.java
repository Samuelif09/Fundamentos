package com.openlib.market.application.almacenamiento;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.almacenamiento.ArchivoImagen;
import com.openlib.market.domain.almacenamiento.IAlmacenamientoGateway;

@Service
public class SubirImagenLibroInteractor implements ISubirImagenLibroUseCase {

    private final IAlmacenamientoGateway almacenamientoGateway;

    public SubirImagenLibroInteractor(IAlmacenamientoGateway almacenamientoGateway) {
        this.almacenamientoGateway = almacenamientoGateway;
    }

    @Override
    public String subirPortada(String idLibro, ArchivoImagen archivo) {
        if (idLibro == null || idLibro.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del libro es obligatorio");
        }
        // La validación del ArchivoImagen ya se hizo en el Value Object (mime, tamaño)
        // El Interactor solo orquesta el guardado
        return almacenamientoGateway.guardar(archivo, "portadas/" + idLibro);
    }
}
