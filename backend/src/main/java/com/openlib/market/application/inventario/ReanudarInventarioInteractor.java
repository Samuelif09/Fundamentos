package com.openlib.market.application.inventario;

import com.openlib.market.domain.detalle.ILibroPublicacionGateway;
import com.openlib.market.domain.detalle.Libro;
import java.util.Optional;

public class ReanudarInventarioInteractor implements IReanudarInventarioUseCase {

    private final ILibroPublicacionGateway libroGateway;

    public ReanudarInventarioInteractor(ILibroPublicacionGateway libroGateway) {
        this.libroGateway = libroGateway;
    }

    @Override
    public void reanudar(String idVendedor, String isbn) {
        Optional<Libro> libroOpt = libroGateway.obtenerPorIsbn(isbn);
        if (libroOpt.isEmpty()) {
            throw new IllegalArgumentException("Libro no encontrado.");
        }

        Libro libro = libroOpt.get();
        if (!libro.getIdVendedor().equals(idVendedor)) {
            throw new IllegalStateException("Solo el propietario puede reanudar este libro.");
        }

        Libro libroReanudado = libro.reanudar();
        libroGateway.actualizar(libroReanudado);
    }
}
