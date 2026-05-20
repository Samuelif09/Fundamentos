package com.openlib.market.application.resena;

import com.openlib.market.domain.detalle.ILibroPublicacionGateway;
import com.openlib.market.domain.detalle.Libro;
import com.openlib.market.domain.resena.ComentarioRespuesta;
import com.openlib.market.domain.resena.IResenaGateway;
import com.openlib.market.domain.resena.Resena;

import java.util.Optional;

public class ResponderReputacionInteractor implements IResponderReputacionUseCase {

    private final IResenaGateway resenaGateway;
    private final ILibroPublicacionGateway libroGateway;

    public ResponderReputacionInteractor(IResenaGateway resenaGateway, ILibroPublicacionGateway libroGateway) {
        this.resenaGateway = resenaGateway;
        this.libroGateway = libroGateway;
    }

    @Override
    public void responder(String idVendedor, String idResena, String comentario) {
        Optional<Resena> resenaOpt = resenaGateway.obtenerPorId(idResena);
        if (resenaOpt.isEmpty()) {
            throw new IllegalArgumentException("Reseña no encontrada");
        }

        Resena resena = resenaOpt.get();

        // Validar que el vendedor es dueño del libro reseñado
        Optional<Libro> libroOpt = libroGateway.obtenerPorIsbn(resena.getIsbnLibro());
        if (libroOpt.isEmpty() || !idVendedor.equals(libroOpt.get().getIdVendedor())) {
            throw new IllegalStateException("El vendedor no tiene permisos para responder esta reseña");
        }

        // El dominio valida respuesta duplicada y vacía
        ComentarioRespuesta respuesta = new ComentarioRespuesta(comentario);
        resena.responder(respuesta);

        resenaGateway.actualizar(resena);
    }
}
