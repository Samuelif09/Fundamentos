package com.openlib.market.application.catalogo;

import com.openlib.market.domain.catalogo.ICatalogoGateway;
import com.openlib.market.domain.catalogo.PaginaDominio;
import com.openlib.market.domain.catalogo.Paginacion;

import java.util.List;
import java.util.stream.Collectors;

public class VerCatalogoInteractor implements IVerCatalogoUseCase {

    private final ICatalogoGateway catalogoGateway;

    public VerCatalogoInteractor(ICatalogoGateway catalogoGateway) {
        this.catalogoGateway = catalogoGateway;
    }

    @Override
    public CatalogoPaginadoResponse verCatalogo(int pagina, int tamano) {
        Paginacion paginacion = new Paginacion(pagina, tamano);
        var paginaDominio = catalogoGateway.listarPaginado(paginacion);

        List<LibroCatalogoDto> librosDto = paginaDominio.getContenido().stream()
                .map(l -> new LibroCatalogoDto(l.isbn(), l.titulo(), l.precio(), l.urlPortada()))
                .collect(Collectors.toList());

        return new CatalogoPaginadoResponse(
                librosDto,
                paginaDominio.getPaginaActual(),
                paginaDominio.getTotalPaginas(),
                paginaDominio.getTotalElementos(),
                paginaDominio.hasNext()
        );
    }
}
