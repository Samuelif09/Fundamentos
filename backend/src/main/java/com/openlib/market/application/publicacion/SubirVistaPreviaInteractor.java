package com.openlib.market.application.publicacion;

import com.openlib.market.domain.almacenamiento.ArchivoVistaPrevia;
import com.openlib.market.domain.almacenamiento.IAlmacenamientoVistaPreviaGateway;
import com.openlib.market.domain.detalle.ILibroPublicacionGateway;
import com.openlib.market.domain.detalle.Libro;

import java.util.Optional;

public class SubirVistaPreviaInteractor implements ISubirVistaPreviaUseCase {

    private final ILibroPublicacionGateway libroGateway;
    private final IAlmacenamientoVistaPreviaGateway almacenamientoGateway;

    public SubirVistaPreviaInteractor(ILibroPublicacionGateway libroGateway, IAlmacenamientoVistaPreviaGateway almacenamientoGateway) {
        this.libroGateway = libroGateway;
        this.almacenamientoGateway = almacenamientoGateway;
    }

    @Override
    public void subirVistaPrevia(String idVendedor, String isbn, byte[] contenidoArchivo, String tipoMime) {
        Optional<Libro> libroOpt = libroGateway.obtenerPorIsbn(isbn);
        if (libroOpt.isEmpty()) {
            throw new IllegalArgumentException("Libro no encontrado");
        }

        Libro libro = libroOpt.get();
        if (!libro.getIdVendedor().equals(idVendedor)) {
            throw new IllegalStateException("El vendedor no tiene permisos para modificar este libro");
        }

        ArchivoVistaPrevia archivo = new ArchivoVistaPrevia(contenidoArchivo, tipoMime);
        String urlVistaPrevia = almacenamientoGateway.guardar(archivo, "preview_" + isbn);

        Libro libroActualizado = new Libro(
                libro.getIsbn(),
                libro.getTitulo(),
                libro.getSinopsis(),
                libro.getPrecio(),
                libro.getUrlPortada(),
                libro.getCategoria(),
                libro.getIdVendedor(),
                libro.getEstado(),
                urlVistaPrevia
        );

        libroGateway.actualizar(libroActualizado);
    }
}
