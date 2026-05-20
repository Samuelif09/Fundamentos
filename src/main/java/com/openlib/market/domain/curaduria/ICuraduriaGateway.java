package com.openlib.market.domain.curaduria;

import com.openlib.market.domain.detalle.EstadoLibro;
import com.openlib.market.domain.detalle.Libro;
import java.util.List;

public interface ICuraduriaGateway {
    List<Libro> listarPorEstado(EstadoLibro estado, int page, int size);
    void actualizar(Libro libro);
    java.util.Optional<Libro> obtenerPorIsbn(String isbn);
}
