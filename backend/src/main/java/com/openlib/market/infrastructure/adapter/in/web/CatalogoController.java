package com.openlib.market.infrastructure.adapter.in.web;

import com.openlib.market.application.catalogo.CatalogoPaginadoResponse;
import com.openlib.market.application.catalogo.IBuscarCatalogoUseCase;
import com.openlib.market.application.catalogo.IVerCatalogoUseCase;
import com.openlib.market.application.catalogo.LibroCatalogoDto;
import com.openlib.market.application.detalle.IVerDetalleLibroUseCase;
import com.openlib.market.application.detalle.LibroDetalleCompradorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/libros")
public class CatalogoController {

    private final IVerCatalogoUseCase verCatalogoUseCase;
    private final IBuscarCatalogoUseCase buscarCatalogoUseCase;
    private final IVerDetalleLibroUseCase verDetalleLibroUseCase;

    public CatalogoController(IVerCatalogoUseCase verCatalogoUseCase,
                              IBuscarCatalogoUseCase buscarCatalogoUseCase,
                              IVerDetalleLibroUseCase verDetalleLibroUseCase) {
        this.verCatalogoUseCase = verCatalogoUseCase;
        this.buscarCatalogoUseCase = buscarCatalogoUseCase;
        this.verDetalleLibroUseCase = verDetalleLibroUseCase;
    }

    @GetMapping
    public ResponseEntity<?> obtenerCatalogo(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {
        
        if ((search != null && !search.isEmpty()) || (category != null && !category.isEmpty())) {
            // Busqueda filtrada
            List<LibroCatalogoDto> resultados = buscarCatalogoUseCase.buscar(search, null, category, null, null);
            return ResponseEntity.ok(resultados);
        } else {
            // Catalogo general paginado
            CatalogoPaginadoResponse response = verCatalogoUseCase.verCatalogo(pagina, tamano);
            return ResponseEntity.ok(response.getLibros());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroDetalleCompradorDto> obtenerDetalleLibro(@PathVariable String id) {
        LibroDetalleCompradorDto detalle = verDetalleLibroUseCase.verDetalle(id);
        return ResponseEntity.ok(detalle);
    }
}
