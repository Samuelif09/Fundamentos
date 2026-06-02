package com.openlib.market.application.biblioteca;

import com.openlib.market.domain.biblioteca.IBibliotecaGateway;
import com.openlib.market.domain.catalogo.LibroCatalogo;
import java.util.List;

public class VerBibliotecaInteractor implements IVerBibliotecaUseCase {

    private final IBibliotecaGateway bibliotecaGateway;

    public VerBibliotecaInteractor(IBibliotecaGateway bibliotecaGateway) {
        this.bibliotecaGateway = bibliotecaGateway;
    }

    @Override
    public List<LibroCatalogo> obtenerBibliotecaUsuario(String idUsuario) {
        return bibliotecaGateway.listarLibrosComprados(idUsuario);
    }
}
