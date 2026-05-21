package com.openlib.market.application.catalogo;

public interface IVerCatalogoUseCase {
    CatalogoPaginadoResponse verCatalogo(int pagina, int tamano);
}
