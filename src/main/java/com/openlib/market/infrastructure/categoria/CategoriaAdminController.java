package com.openlib.market.infrastructure.categoria;

import com.openlib.market.application.categoria.IGestionarCategoriasUseCase;
import com.openlib.market.domain.categoria.CategoriaCatalogo;
import com.openlib.market.domain.categoria.CategoriaDuplicadaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/categorias")
public class CategoriaAdminController {

    private final IGestionarCategoriasUseCase gestionarUseCase;

    public CategoriaAdminController(IGestionarCategoriasUseCase gestionarUseCase) {
        this.gestionarUseCase = gestionarUseCase;
    }

    @PostMapping
    public ResponseEntity<?> crearCategoria(@RequestBody CrearCategoriaRequest request) {
        try {
            CategoriaCatalogo cat = gestionarUseCase.crearCategoria(request.nombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(new CategoriaResponse(cat.getId(), cat.getNombre().getValor(), cat.getEstado().name()));
        } catch (CategoriaDuplicadaException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarCategoria(@PathVariable String id, @RequestBody CrearCategoriaRequest request) {
        try {
            CategoriaCatalogo cat = gestionarUseCase.editarCategoria(id, request.nombre());
            return ResponseEntity.ok(new CategoriaResponse(cat.getId(), cat.getNombre().getValor(), cat.getEstado().name()));
        } catch (CategoriaDuplicadaException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<String> cambiarEstado(@PathVariable String id, @RequestBody CambiarEstadoRequest request) {
        try {
            gestionarUseCase.cambiarEstado(id, request.estado());
            return ResponseEntity.ok("Estado actualizado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarTodas() {
        List<CategoriaResponse> categorias = gestionarUseCase.listarTodas().stream()
                .map(c -> new CategoriaResponse(c.getId(), c.getNombre().getValor(), c.getEstado().name()))
                .toList();
        return ResponseEntity.ok(categorias);
    }

    public record CrearCategoriaRequest(String nombre) {}
    public record CambiarEstadoRequest(String estado) {}
    public record CategoriaResponse(String id, String nombre, String estado) {}
}
