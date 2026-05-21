package com.openlib.market.domain.filtroprecio;

import java.util.List;

public interface IFiltroPrecioGateway {
    List<LibroFiltro> buscarPorRango(RangoPrecio rango);
}
