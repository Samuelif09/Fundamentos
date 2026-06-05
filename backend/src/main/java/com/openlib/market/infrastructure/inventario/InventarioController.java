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

    @org.springframework.beans.factory.annotation.Autowired
    private jakarta.persistence.EntityManager entityManager;

    @GetMapping("/migrate")
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<String> migrarDatos() {
        int updated = entityManager.createNativeQuery(
                "UPDATE contenidos_digitales SET id_vendedor = '123e4567-e89b-12d3-a456-426614174000'"
        ).executeUpdate();
        return ResponseEntity.ok("Se migraron " + updated + " libros al UUID 123e4567-e89b-12d3-a456-426614174000 exitosamente.");
    }
}
