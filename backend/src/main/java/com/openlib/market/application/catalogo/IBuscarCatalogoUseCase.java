package com.openlib.market.application.catalogo;

import java.util.List;

public interface IBuscarCatalogoUseCase {
    List<LibroCatalogoDto> buscar(String titulo, String autor, String categoria, Double precioMin, Double precioMax);
}
