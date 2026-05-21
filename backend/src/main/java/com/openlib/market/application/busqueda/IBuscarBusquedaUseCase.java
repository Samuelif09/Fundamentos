package com.openlib.market.application.busqueda;

import java.util.List;

public interface IBuscarBusquedaUseCase {
    List<LibroBuscadoDto> buscarPorPalabrasClave(String query);
}
