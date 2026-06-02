package com.openlib.market.domain.biblioteca;

import java.util.List;
import com.openlib.market.domain.catalogo.LibroCatalogo;

public interface IBibliotecaGateway {
    boolean validarLicencia(LicenciaAcceso licencia);
    List<LibroCatalogo> listarLibrosComprados(String idUsuario);
}
