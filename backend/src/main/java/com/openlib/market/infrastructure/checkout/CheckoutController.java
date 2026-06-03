package com.openlib.market.infrastructure.checkout;

import com.openlib.market.application.checkout.ProcesarCheckoutInteractor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

    private final ProcesarCheckoutInteractor procesarCheckoutInteractor;

    public CheckoutController(ProcesarCheckoutInteractor procesarCheckoutInteractor) {
        this.procesarCheckoutInteractor = procesarCheckoutInteractor;
    }

    @PostMapping("/{sesionId}")
    public ResponseEntity<Void> checkout(@PathVariable String sesionId, @RequestBody CheckoutRequestDTO request) {
        procesarCheckoutInteractor.ejecutar(sesionId, request.getIdUsuario(), request.getMetodoPago());
        return ResponseEntity.ok().build();
    }
}
