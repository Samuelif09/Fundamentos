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

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponseDto> procesarCheckout(@RequestBody CheckoutFrontendRequestDto frontRequest) {
        // En una implementación real con Autenticación (Spring Security), el idUsuario se obtendría del JWT en contexto.
        // Simulamos valores fijos para completar el CheckoutFlow del Frontend por ahora.
        String idUsuario = "usuario-autenticado-123";
        String idPedidoGenerado = UUID.randomUUID().toString().substring(0, 8);

        // Mapeamos a la petición del backend (mockeando el carrito actual y monto)
        CheckoutRequestDto backRequest = new CheckoutRequestDto(
                idUsuario,
                idPedidoGenerado,
                15000.0, // Monto simulado
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
