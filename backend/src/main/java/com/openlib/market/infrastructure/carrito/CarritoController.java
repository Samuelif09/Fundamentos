package com.openlib.market.infrastructure.carrito;

import com.openlib.market.application.carrito.AgregarItemRequestDto;
import com.openlib.market.application.carrito.IAgregarCarritoUseCase;
import com.openlib.market.application.carrito.IVerCarritoUseCase;
import com.openlib.market.application.carrito.IActualizarItemCarritoUseCase;
import com.openlib.market.application.carrito.CartDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carrito")
public class CarritoController {

    private final IAgregarCarritoUseCase agregarCarritoUseCase;
    private final IVerCarritoUseCase verCarritoUseCase;
    private final IActualizarItemCarritoUseCase actualizarItemCarritoUseCase;

    public CarritoController(IAgregarCarritoUseCase agregarCarritoUseCase,
                             IVerCarritoUseCase verCarritoUseCase,
                             IActualizarItemCarritoUseCase actualizarItemCarritoUseCase) {
        this.agregarCarritoUseCase = agregarCarritoUseCase;
        this.verCarritoUseCase = verCarritoUseCase;
        this.actualizarItemCarritoUseCase = actualizarItemCarritoUseCase;
    }

    @PostMapping("/items")
    public ResponseEntity<String> agregarItemVisitante(@RequestBody AgregarItemRequestDto request) {
        try {
            agregarCarritoUseCase.agregarAlCarrito(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"Item agregado al carrito de visitante exitosamente\"}");
        } catch (com.openlib.market.domain.carrito.StockInsuficienteException e) {
            return ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/usuarios/{userId}/carrito/items")
    public ResponseEntity<String> agregarItemUsuario(@PathVariable("userId") String userId, @RequestBody AgregarItemRequestDto request) {
        try {
            request.setIdUsuario(userId); // Inyectar ID del path (o del token)
            agregarCarritoUseCase.agregarAlCarrito(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\":\"Item agregado al carrito de usuario exitosamente\"}");
        } catch (com.openlib.market.domain.carrito.StockInsuficienteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuarios/{userId}/carrito")
    public ResponseEntity<CartDto> obtenerCarritoUsuario(@PathVariable("userId") String userId) {
        CartDto cart = verCarritoUseCase.verCarrito(userId);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/usuarios/{userId}/carrito/items/{isbn}")
    public ResponseEntity<?> actualizarCantidadUsuario(
            @PathVariable("userId") String userId,
            @PathVariable("isbn") String isbn,
            @RequestBody java.util.Map<String, Integer> body) {
        try {
            int cantidad = body.getOrDefault("cantidad", 1);
            actualizarItemCarritoUseCase.actualizarCantidad(userId, isbn, cantidad);
            return ResponseEntity.ok().build();
        } catch (com.openlib.market.domain.carrito.StockInsuficienteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/usuarios/{userId}/carrito/items/{isbn}")
    public ResponseEntity<Void> eliminarItemUsuario(
            @PathVariable("userId") String userId,
            @PathVariable("isbn") String isbn) {
        actualizarItemCarritoUseCase.eliminarItem(userId, isbn);
        return ResponseEntity.ok().build();
    }
}
