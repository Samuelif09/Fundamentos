package com.openlib.market.application.comparte;

import com.openlib.market.domain.comparte.EnlaceCompartir;
import com.openlib.market.domain.comparte.ILibroComparteGateway;
import com.openlib.market.domain.comparte.LibroNoDisponibleException;

public class CompartirComparteInteractor implements ICompartirComparteUseCase {

    private final ILibroComparteGateway libroGateway;

    public CompartirComparteInteractor(ILibroComparteGateway libroGateway) {
        this.libroGateway = libroGateway;
    }

    @Override
    public EnlaceDto generarEnlace(String isbn) {
        if (!libroGateway.existeLibroActivo(isbn)) {
            throw new LibroNoDisponibleException("El libro con ISBN " + isbn + " no está disponible o no existe.");
        }

        EnlaceCompartir enlace = new EnlaceCompartir(isbn);

        return new EnlaceDto(enlace.getUrl());
    }
}
