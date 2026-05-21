package com.openlib.market.application.categoria;

import com.openlib.market.domain.categoria.*;

import java.util.List;

public class GestionarCategoriasInteractor implements IGestionarCategoriasUseCase {

    private final ICategoriaGateway categoriaGateway;

    public GestionarCategoriasInteractor(ICategoriaGateway categoriaGateway) {
        this.categoriaGateway = categoriaGateway;
    }

    @Override
    public CategoriaCatalogo crearCategoria(String nombre) {
        NombreCategoria nombreCategoria = new NombreCategoria(nombre);
        if (categoriaGateway.existePorNombreNormalizado(nombreCategoria.getNormalizado())) {
            throw new CategoriaDuplicadaException("Ya existe una categoría con el nombre: " + nombreCategoria.getValor());
        }
        CategoriaCatalogo categoria = new CategoriaCatalogo(nombreCategoria);
        categoriaGateway.guardar(categoria);
        return categoria;
    }

    @Override
    public CategoriaCatalogo editarCategoria(String id, String nuevoNombre) {
        CategoriaCatalogo categoria = categoriaGateway.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        NombreCategoria nuevoNombreCat = new NombreCategoria(nuevoNombre);
        if (categoriaGateway.existePorNombreNormalizado(nuevoNombreCat.getNormalizado())) {
            throw new CategoriaDuplicadaException("Ya existe una categoría con el nombre: " + nuevoNombreCat.getValor());
        }

        categoria.editarNombre(nuevoNombreCat);
        categoriaGateway.actualizar(categoria);
        return categoria;
    }

    @Override
    public void cambiarEstado(String id, String estado) {
        CategoriaCatalogo categoria = categoriaGateway.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        if ("ACTIVA".equalsIgnoreCase(estado)) {
            categoria.activar();
        } else if ("INACTIVA".equalsIgnoreCase(estado)) {
            categoria.desactivar();
        } else {
            throw new IllegalArgumentException("Estado inválido: " + estado);
        }
        categoriaGateway.actualizar(categoria);
    }

    @Override
    public List<CategoriaCatalogo> listarTodas() {
        return categoriaGateway.listarTodas();
    }
}
