package com.openlib.market.application.catalogo;

import com.openlib.market.domain.catalogo.CriterioBusqueda;
import com.openlib.market.domain.catalogo.ICatalogoGateway;

import java.util.List;
import java.util.stream.Collectors;

public class BuscarCatalogoInteractor implements IBuscarCatalogoUseCase {

    private final ICatalogoGateway catalogoGateway;

    public BuscarCatalogoInteractor(ICatalogoGateway catalogoGateway) {
        this.catalogoGateway = catalogoGateway;
    }

    @Override
    public List<LibroCatalogoDto> buscar(String titulo, String autor, String categoria, Double precioMin, Double precioMax) {
        com.openlib.market.domain.filtroprecio.RangoPrecio rango = null;
        if (precioMin != null && precioMax != null) {
            rango = new com.openlib.market.domain.filtroprecio.RangoPrecio(precioMin, precioMax);
        }

        CriterioBusqueda criterio = new CriterioBusqueda(titulo, autor, categoria, rango);
        
        return catalogoGateway.buscarPorFiltros(criterio).stream()
                .map(l -> new LibroCatalogoDto(l.isbn(), l.titulo(), l.precio(), l.urlPortada()))
                .collect(Collectors.toList());
    }
}
