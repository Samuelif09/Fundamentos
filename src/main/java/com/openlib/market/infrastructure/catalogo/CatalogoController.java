package com.openlib.market.infrastructure.catalogo;

import com.openlib.market.application.catalogo.CatalogoPaginadoResponse;
import com.openlib.market.application.catalogo.IVerCatalogoUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import com.openlib.market.application.catalogo.LibroCatalogoDto;

@RestController
@RequestMapping("/api/v1/catalogo")
public class CatalogoController {

    private final IVerCatalogoUseCase verCatalogoUseCase;
    private final com.openlib.market.application.catalogo.IBuscarCatalogoUseCase buscarCatalogoUseCase;

    public CatalogoController(IVerCatalogoUseCase verCatalogoUseCase, com.openlib.market.application.catalogo.IBuscarCatalogoUseCase buscarCatalogoUseCase) {
        this.verCatalogoUseCase = verCatalogoUseCase;
        this.buscarCatalogoUseCase = buscarCatalogoUseCase;
    }

    @GetMapping
    public ResponseEntity<?> verCatalogoPaginado(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        try {
            CatalogoPaginadoResponse response = verCatalogoUseCase.verCatalogo(page, size);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/busqueda-avanzada")
    public ResponseEntity<List<LibroCatalogoDto>> busquedaAvanzada(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String autor,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax) {
        try {
            List<LibroCatalogoDto> resultados = buscarCatalogoUseCase.buscar(titulo, autor, categoria, precioMin, precioMax);
            return ResponseEntity.ok(resultados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
