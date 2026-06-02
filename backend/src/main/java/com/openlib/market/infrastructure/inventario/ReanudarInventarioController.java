package com.openlib.market.infrastructure.inventario;

import com.openlib.market.application.inventario.IReanudarInventarioUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vendedores")
public class ReanudarInventarioController {

    private final IReanudarInventarioUseCase reanudarInventarioUseCase;

    public ReanudarInventarioController(IReanudarInventarioUseCase reanudarInventarioUseCase) {
        this.reanudarInventarioUseCase = reanudarInventarioUseCase;
    }

    @PatchMapping("/{sellerId}/libros/{isbn}/reanudar")
    public ResponseEntity<String> reanudarLibro(@PathVariable String sellerId, @PathVariable String isbn) {
        try {
            reanudarInventarioUseCase.reanudar(sellerId, isbn);
            return ResponseEntity.ok("Libro reanudado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
