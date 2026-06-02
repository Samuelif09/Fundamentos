package com.openlib.market.infrastructure.adapter.in.web;

import com.openlib.market.application.pago.CheckoutRequestDto;
import com.openlib.market.application.pago.IIngresarCheckoutUseCase;
import com.openlib.market.infrastructure.adapter.in.web.dto.CheckoutFrontendRequestDto;
import com.openlib.market.infrastructure.adapter.in.web.dto.CheckoutResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final IIngresarCheckoutUseCase ingresarCheckoutUseCase;

    public PedidoController(IIngresarCheckoutUseCase ingresarCheckoutUseCase) {
        this.ingresarCheckoutUseCase = ingresarCheckoutUseCase;
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<CheckoutResponseDto> procesarCheckout(
            @PathVariable("userId") String idUsuario,
            @RequestBody CheckoutFrontendRequestDto frontRequest) {

        String idPedidoGenerado = UUID.randomUUID().toString().substring(0, 8);

        // Mapeamos a la petición del backend (monto ya no es necesario pasarlo quemado, el interactor lo calculará)
        CheckoutRequestDto backRequest = new CheckoutRequestDto(
                idUsuario,
                idPedidoGenerado,
                0.0, // Monto calculado en el interactor
                frontRequest.getPaymentMethod()
        );

        try {
            // El caso de uso actual devuelve void
            ingresarCheckoutUseCase.procesarCheckout(backRequest);

            // Respondemos al frontend con el formato exacto que espera
            return ResponseEntity.ok(new CheckoutResponseDto(
                    "COMPLETED",
                    idPedidoGenerado,
                    "Pago procesado exitosamente."
            ));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new CheckoutResponseDto(
                    "FAILED",
                    idPedidoGenerado,
                    ex.getMessage()
            ));
        }
    }
}
