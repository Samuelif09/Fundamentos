package com.openlib.market.application.inventario;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.detalle.ILibroPublicacionGateway;
import com.openlib.market.domain.detalle.Libro;

import java.util.Optional;

@Service
public class DespublicarInventarioInteractor implements IDespublicarInventarioUseCase {

    private final ILibroPublicacionGateway libroGateway;

    public DespublicarInventarioInteractor(ILibroPublicacionGateway libroGateway) {
        this.libroGateway = libroGateway;
    }

    @Override
    public void despublicar(String idVendedor, String isbn) {
        if (idVendedor == null || isbn == null) {
            throw new IllegalArgumentException("Vendedor e ISBN son obligatorios");
        }

        Optional<Libro> libroOpt = libroGateway.obtenerPorIsbn(isbn);
        if (libroOpt.isEmpty()) {
            throw new IllegalArgumentException("El libro no existe");
        }

        Libro libro = libroOpt.get();

        if (!idVendedor.equals(libro.getIdVendedor())) {
            throw new IllegalStateException("Acceso denegado: el vendedor no es propietario del libro");
        }

        Libro libroPausado = libro.pausar();
        libroGateway.actualizar(libroPausado);
    }
}
