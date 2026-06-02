package com.openlib.market.application.biblioteca;

import com.openlib.market.domain.catalogo.LibroCatalogo;
import java.util.List;

public interface IVerBibliotecaUseCase {
    List<LibroCatalogo> obtenerBibliotecaUsuario(String idUsuario);
}
