package com.openlib.market.infrastructure.inventario;

import com.openlib.market.application.inventario.IVerInventarioUseCase;
import com.openlib.market.application.inventario.LibroInventarioDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vendedores")
public class InventarioController {

    private final IVerInventarioUseCase verInventarioUseCase;

    public InventarioController(IVerInventarioUseCase verInventarioUseCase) {
        this.verInventarioUseCase = verInventarioUseCase;
    }

    @GetMapping("/{id}/libros")
    public ResponseEntity<List<LibroInventarioDto>> listarLibros(@PathVariable String id) {
        List<LibroInventarioDto> libros = verInventarioUseCase.listarPorVendedor(id);
        return ResponseEntity.ok(libros);
    }
}
