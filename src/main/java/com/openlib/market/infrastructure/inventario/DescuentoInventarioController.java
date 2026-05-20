package com.openlib.market.infrastructure.inventario;

import com.openlib.market.application.inventario.ICrearDescuentoInventarioUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/vendedores")
public class DescuentoInventarioController {

    private final ICrearDescuentoInventarioUseCase crearDescuentoInventarioUseCase;

    public DescuentoInventarioController(ICrearDescuentoInventarioUseCase crearDescuentoInventarioUseCase) {
        this.crearDescuentoInventarioUseCase = crearDescuentoInventarioUseCase;
    }

    @PostMapping("/{idVendedor}/libros/{isbn}/promociones")
    public ResponseEntity<Void> crearPromocion(
            @PathVariable String idVendedor,
            @PathVariable String isbn,
            @RequestBody PromocionRequest req) {
        try {
            crearDescuentoInventarioUseCase.crearDescuento(
                    idVendedor,
                    isbn,
                    req.porcentaje(),
                    req.fechaInicio(),
                    req.fechaFin()
            );
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).build(); // Conflict si se solapa o no tiene permisos
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public record PromocionRequest(int porcentaje, LocalDate fechaInicio, LocalDate fechaFin) {}
}
