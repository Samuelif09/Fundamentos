package com.openlib.market.application.inventario;

import com.openlib.market.domain.detalle.AccesoDenegadoLibroException;
import com.openlib.market.domain.detalle.IActualizarLibroGateway;
import com.openlib.market.domain.detalle.Isbn;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.detalle.Precio;

import java.util.Optional;

public class ActualizarInventarioInteractor implements IActualizarInventarioUseCase {

    private final IActualizarLibroGateway libroGateway;

    public ActualizarInventarioInteractor(IActualizarLibroGateway libroGateway) {
        this.libroGateway = libroGateway;
    }

    @Override
    public void actualizarPrecio(ActualizarPrecioRequestDto request) {
        Isbn isbn = new Isbn(request.getIsbn());
        Optional<Libro> libroOpt = libroGateway.buscarPorIsbn(isbn);

        if (libroOpt.isEmpty()) {
            throw new IllegalArgumentException("Libro no encontrado: " + request.getIsbn());
        }

        Libro libro = libroOpt.get();

        // Verificar propiedad: solo el vendedor dueño puede modificar el precio
        if (!request.getIdVendedor().equals(libro.getIdVendedor())) {
            throw new AccesoDenegadoLibroException("El vendedor no tiene permiso para modificar este libro");
        }

        // Delegar al método de comportamiento del Agregado (DDD)
        Precio nuevoPrecio = new Precio(request.getNuevoPrecio());
        Libro libroActualizado = libro.actualizarPrecio(nuevoPrecio);

        libroGateway.actualizar(libroActualizado);
    }
}
