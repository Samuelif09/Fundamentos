package com.openlib.market.application.filtroprecio;

import java.util.List;

public interface IFiltrarPorPrecioUseCase {
    List<LibroBuscadoDto> filtrar(double min, double max);
}
