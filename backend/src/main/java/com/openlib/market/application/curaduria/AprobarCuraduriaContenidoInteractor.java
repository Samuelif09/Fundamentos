package com.openlib.market.application.curaduria;

import com.openlib.market.domain.curaduria.ICuraduriaGateway;
import com.openlib.market.domain.detalle.Libro;
import java.util.Optional;

public class AprobarCuraduriaContenidoInteractor implements IAprobarCuraduriaContenidoUseCase {

    private final ICuraduriaGateway curaduriaGateway;

    public AprobarCuraduriaContenidoInteractor(ICuraduriaGateway curaduriaGateway) {
        this.curaduriaGateway = curaduriaGateway;
    }

    @Override
    public void aprobarLibro(String isbn) {
        Optional<Libro> libroOpt = curaduriaGateway.obtenerPorIsbn(isbn);
        if (libroOpt.isEmpty()) {
            throw new IllegalArgumentException("Libro no encontrado");
        }
        
        Libro libroAprobado = libroOpt.get().aprobar();
        curaduriaGateway.actualizar(libroAprobado);
    }
}
