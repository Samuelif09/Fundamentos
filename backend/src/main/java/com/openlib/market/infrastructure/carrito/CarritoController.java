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
    private final com.openlib.market.application.carrito.IVerCarritoUseCase verCarritoUseCase;
    private final com.openlib.market.application.carrito.IActualizarCarritoUseCase actualizarCarritoUseCase;

    public CarritoController(
            IAgregarCarritoUseCase agregarCarritoUseCase, 
            com.openlib.market.application.carrito.IVerCarritoUseCase verCarritoUseCase,
            com.openlib.market.application.carrito.IActualizarCarritoUseCase actualizarCarritoUseCase
    ) {
        this.agregarCarritoUseCase = agregarCarritoUseCase;
        this.verCarritoUseCase = verCarritoUseCase;
        this.actualizarCarritoUseCase = actualizarCarritoUseCase;
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

    @GetMapping("/usuarios/{userId}/carrito")
    public ResponseEntity<com.openlib.market.application.carrito.CarritoResponseDto> verCarrito(@PathVariable String userId) {
        return ResponseEntity.ok(verCarritoUseCase.verCarritoUsuario(userId));
    }

    @DeleteMapping("/usuarios/{userId}/carrito/items/{isbn}")
    public ResponseEntity<Void> eliminarItem(@PathVariable String userId, @PathVariable String isbn) {
        try {
            actualizarCarritoUseCase.eliminarItem(userId, isbn);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
