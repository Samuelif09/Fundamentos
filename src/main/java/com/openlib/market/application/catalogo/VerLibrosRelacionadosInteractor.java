package com.openlib.market.application.catalogo;

import com.openlib.market.domain.catalogo.CriterioSimilitud;
import com.openlib.market.domain.catalogo.ICatalogoGateway;
import com.openlib.market.domain.detalle.IDetalleGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.LibroNoEncontradoException;

import java.util.List;

public class VerLibrosRelacionadosInteractor implements IVerLibrosRelacionadosUseCase {

    private static final int MAX_RELACIONADOS = 5;

    private final ICatalogoGateway catalogoGateway;
    private final IDetalleGateway detalleGateway;

    public VerLibrosRelacionadosInteractor(ICatalogoGateway catalogoGateway, IDetalleGateway detalleGateway) {
        this.catalogoGateway = catalogoGateway;
        this.detalleGateway = detalleGateway;
    }

    @Override
    public List<LibroCatalogoDto> verRelacionados(String isbn) {
        Isbn isbnDomain = new Isbn(isbn);
        Libro libro = detalleGateway.buscarPorId(isbnDomain)
                .orElseThrow(() -> new LibroNoEncontradoException("Libro con ISBN " + isbn + " no encontrado"));

        String categoria = libro.getCategoria() != null ? libro.getCategoria() : libro.getTitulo();
        CriterioSimilitud criterio = new CriterioSimilitud(isbn, categoria, MAX_RELACIONADOS);

        return catalogoGateway.buscarRelacionados(criterio).stream()
                .map(l -> new LibroCatalogoDto(l.isbn(), l.titulo(), l.precio(), l.urlPortada()))
                .toList();
    }
}
