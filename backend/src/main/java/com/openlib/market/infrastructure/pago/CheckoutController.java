package com.openlib.market.infrastructure.pago;

import com.openlib.market.application.pago.CheckoutRequestDto;
import com.openlib.market.application.pago.IIngresarCheckoutUseCase;
import com.openlib.market.domain.pago.PagoRechazadoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

    private final IIngresarCheckoutUseCase checkoutUseCase;

    public CheckoutController(IIngresarCheckoutUseCase checkoutUseCase) {
        this.checkoutUseCase = checkoutUseCase;
    }

    @PostMapping("/pagar")
    public ResponseEntity<String> procesarPago(@RequestBody CheckoutRequestDto request) {
        try {
            checkoutUseCase.procesarCheckout(request);
            return ResponseEntity.ok("El pago fue procesado exitosamente y su pedido está completado.");
        } catch (PagoRechazadoException e) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
