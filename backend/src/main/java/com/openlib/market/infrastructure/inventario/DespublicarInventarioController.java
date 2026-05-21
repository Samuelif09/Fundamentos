package com.openlib.market.infrastructure.inventario;

import com.openlib.market.application.inventario.IDespublicarInventarioUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vendedores")
public class DespublicarInventarioController {

    private final IDespublicarInventarioUseCase despublicarInventarioUseCase;

    public DespublicarInventarioController(IDespublicarInventarioUseCase despublicarInventarioUseCase) {
        this.despublicarInventarioUseCase = despublicarInventarioUseCase;
    }

    @PatchMapping("/{idVendedor}/libros/{isbn}/estado")
    public ResponseEntity<Void> actualizarEstado(
            @PathVariable String idVendedor,
            @PathVariable String isbn,
            @RequestBody EstadoRequest req) {
        try {
            if ("PAUSADO".equalsIgnoreCase(req.estado())) {
                despublicarInventarioUseCase.despublicar(idVendedor, isbn);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).build(); // 403 Forbidden para problemas de propiedad
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public record EstadoRequest(String estado) {}
}
