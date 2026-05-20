package com.openlib.market.application.categoria;

import com.openlib.market.domain.categoria.CategoriaCatalogo;
import java.util.List;

public interface IGestionarCategoriasUseCase {
    CategoriaCatalogo crearCategoria(String nombre);
    CategoriaCatalogo editarCategoria(String id, String nuevoNombre);
    void cambiarEstado(String id, String estado);
    List<CategoriaCatalogo> listarTodas();
}
