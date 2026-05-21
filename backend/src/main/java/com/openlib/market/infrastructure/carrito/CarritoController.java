package com.openlib.market.infrastructure.carrito;

import com.openlib.market.application.carrito.AgregarItemRequestDto;
import com.openlib.market.application.carrito.IAgregarCarritoUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carrito")
public class CarritoController {

    private final IAgregarCarritoUseCase agregarCarritoUseCase;

    public CarritoController(IAgregarCarritoUseCase agregarCarritoUseCase) {
        this.agregarCarritoUseCase = agregarCarritoUseCase;
    }

    @PostMapping("/items")
    public ResponseEntity<String> agregarItemVisitante(@RequestBody AgregarItemRequestDto request) {
        try {
            agregarCarritoUseCase.agregarAlCarrito(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Item agregado al carrito de visitante exitosamente");
        } catch (com.openlib.market.domain.carrito.StockInsuficienteException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/usuarios/{userId}/carrito/items")
    public ResponseEntity<String> agregarItemUsuario(@PathVariable("userId") String userId, @RequestBody AgregarItemRequestDto request) {
        try {
            request.setIdUsuario(userId); // Inyectar ID del path (o del token)
            agregarCarritoUseCase.agregarAlCarrito(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Item agregado al carrito de usuario exitosamente (con subtotal decorado)");
        } catch (com.openlib.market.domain.carrito.StockInsuficienteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
