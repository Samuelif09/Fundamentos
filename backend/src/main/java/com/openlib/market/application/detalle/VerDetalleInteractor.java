package com.openlib.market.application.detalle;

import com.openlib.market.domain.detalle.IDetalleGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.LibroNoEncontradoException;

public class VerDetalleInteractor implements IVerDetalleUseCase {

    private final IDetalleGateway detalleGateway;

    public VerDetalleInteractor(IDetalleGateway detalleGateway) {
        this.detalleGateway = detalleGateway;
    }

    @Override
    public LibroDetalleDto verDetalle(String isbnStr) {
        Isbn isbn = new Isbn(isbnStr);
        Libro libro = detalleGateway.buscarPorId(isbn)
                .orElseThrow(() -> new LibroNoEncontradoException("Libro no encontrado con ISBN: " + isbnStr));

        return new LibroDetalleDto(
                libro.getIsbn().getValor(),
                libro.getTitulo(),
                libro.getSinopsis(),
                libro.getPrecio().getValor(),
                libro.getUrlPortada()
        );
    }
}
