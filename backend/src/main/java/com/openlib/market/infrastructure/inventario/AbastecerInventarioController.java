package com.openlib.market.infrastructure.inventario;

import com.openlib.market.application.inventario.IAbastecerInventarioUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/v1/inventarios")
public class AbastecerInventarioController {

    private final IAbastecerInventarioUseCase abastecerInventarioUseCase;

    public AbastecerInventarioController(IAbastecerInventarioUseCase abastecerInventarioUseCase) {
        this.abastecerInventarioUseCase = abastecerInventarioUseCase;
    }

    @PostMapping("/{productoId}/abastecer")
    public ResponseEntity<Void> abastecer(@PathVariable String productoId, @RequestBody AbastecerInventarioRequestDto request) {
        abastecerInventarioUseCase.ejecutar(productoId, request.getCantidad());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
