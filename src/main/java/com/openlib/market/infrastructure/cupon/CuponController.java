package com.openlib.market.infrastructure.cupon;

import com.openlib.market.application.cupon.AplicarCuponRequestDto;
import com.openlib.market.application.cupon.AplicarCuponResponseDto;
import com.openlib.market.application.cupon.IAplicarCuponUseCase;
import com.openlib.market.domain.cupon.CuponExpiradoException;
import com.openlib.market.domain.cupon.CuponNoEncontradoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuarios")
public class CuponController {

    private final IAplicarCuponUseCase aplicarCuponUseCase;

    public CuponController(IAplicarCuponUseCase aplicarCuponUseCase) {
        this.aplicarCuponUseCase = aplicarCuponUseCase;
    }

    @PostMapping("/{userId}/carrito/cupon")
    public ResponseEntity<?> aplicarCupon(
            @PathVariable("userId") String userId,
            @RequestBody Map<String, String> body) {
        
        String codigo = body.get("codigo");
        if (codigo == null || codigo.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El código del cupón es obligatorio");
        }

        try {
            AplicarCuponRequestDto request = new AplicarCuponRequestDto(userId, codigo);
            AplicarCuponResponseDto response = aplicarCuponUseCase.aplicar(request);
            return ResponseEntity.ok(response);
        } catch (CuponNoEncontradoException | CuponExpiradoException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
