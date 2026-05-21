package com.openlib.market.application.catalogo;

import java.util.List;

public interface IVerLibrosRelacionadosUseCase {
    List<LibroCatalogoDto> verRelacionados(String isbn);
}
