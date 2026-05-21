package com.openlib.market.domain.catalogo;

import java.util.List;

public interface ICatalogoGateway {
    PaginaDominio<LibroCatalogo> listarPaginado(Paginacion paginacion);
    List<LibroCatalogo> buscarPorFiltros(CriterioBusqueda criterio);
    List<LibroCatalogo> buscarRelacionados(CriterioSimilitud criterio);
}


