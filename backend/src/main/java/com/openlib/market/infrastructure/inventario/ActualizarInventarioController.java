package com.openlib.market.infrastructure.inventario;

import com.openlib.market.application.inventario.ActualizarPrecioRequestDto;
import com.openlib.market.application.inventario.IActualizarInventarioUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/vendedores")
public class ActualizarInventarioController {

    private final IActualizarInventarioUseCase actualizarInventarioUseCase;

    public ActualizarInventarioController(IActualizarInventarioUseCase actualizarInventarioUseCase) {
        this.actualizarInventarioUseCase = actualizarInventarioUseCase;
    }

    @PatchMapping("/{sellerId}/libros/{isbn}/precio")
    public ResponseEntity<String> actualizarPrecio(
            @PathVariable String sellerId,
            @PathVariable String isbn,
            @RequestBody Map<String, Double> body) {

        Double nuevoPrecio = body.get("precio");
        if (nuevoPrecio == null) {
            return ResponseEntity.badRequest().body("Se requiere el campo 'precio'");
        }

        ActualizarPrecioRequestDto request = new ActualizarPrecioRequestDto(sellerId, isbn, nuevoPrecio);
        actualizarInventarioUseCase.actualizarPrecio(request);
        return ResponseEntity.ok("Precio actualizado exitosamente.");
    }
}
