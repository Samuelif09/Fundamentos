package com.openlib.market.domain.resena;

import java.util.List;
import java.util.Optional;

public interface IResenaGateway {
    List<Resena> buscarResenasPorIsbn(String isbn);
    List<Resena> listarPorLibroId(String isbnLibro, int offset, int limit);
    Optional<Resena> obtenerPorId(String id);
    void actualizar(Resena resena);
}
