package com.openlib.market.infrastructure.pago;

import com.openlib.market.application.pago.IRealizarPagoUseCase;
import com.openlib.market.application.pago.RealizarPagoRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PagoController {

    private final IRealizarPagoUseCase realizarPagoUseCase;

    public PagoController(IRealizarPagoUseCase realizarPagoUseCase) {
        this.realizarPagoUseCase = realizarPagoUseCase;
    }

    @PostMapping("/checkout-directo")
    public ResponseEntity<String> checkoutDirecto(@RequestBody RealizarPagoRequestDto request) {
        // La excepción PagoRechazadoException es atrapada por el ControllerAdvice global
        realizarPagoUseCase.realizarPago(request);
        return ResponseEntity.status(HttpStatus.OK).body("Pago procesado y pedido completado con éxito");
    }
}
