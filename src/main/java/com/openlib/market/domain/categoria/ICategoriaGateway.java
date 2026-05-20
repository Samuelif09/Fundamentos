package com.openlib.market.domain.categoria;

import java.util.List;
import java.util.Optional;

public interface ICategoriaGateway {
    void guardar(CategoriaCatalogo categoria);
    void actualizar(CategoriaCatalogo categoria);
    Optional<CategoriaCatalogo> obtenerPorId(String id);
    boolean existePorNombreNormalizado(String nombreNormalizado);
    List<CategoriaCatalogo> listarTodas();
}
