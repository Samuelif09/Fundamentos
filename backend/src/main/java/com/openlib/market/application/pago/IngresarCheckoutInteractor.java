package com.openlib.market.application.pago;

import org.springframework.stereotype.Service;
import com.openlib.market.domain.pago.*;

import java.util.UUID;

@Service
public class IngresarCheckoutInteractor implements IIngresarCheckoutUseCase {

    private final IPasarelaPagoGateway pasarelaPago;
    private final IEventPublisher eventPublisher;

    public IngresarCheckoutInteractor(IPasarelaPagoGateway pasarelaPago, IEventPublisher eventPublisher) {
        this.pasarelaPago = pasarelaPago;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void procesarCheckout(CheckoutRequestDto request) {
        TokenPago token = new TokenPago(request.getTokenPago());
        Monto monto = new Monto(request.getMontoTotal());

        // El ID de transacción sería generado internamente o retornado por la pasarela, usamos UUID temporal
        TransaccionPago transaccion = new TransaccionPago(UUID.randomUUID().toString(), token, monto);

        boolean cobroExitoso = pasarelaPago.procesarCobro(token, monto);

        if (!cobroExitoso) {
            transaccion.rechazar();
            throw new PagoRechazadoException("La pasarela de pago rechazó la transacción.");
        }

        transaccion.aprobar();

        // Publicar evento de dominio para que el módulo Post-Compra escuche asíncronamente
        PedidoCompletadoEvent evento = new PedidoCompletadoEvent(
                request.getIdPedido(),
                request.getIdUsuario(),
                request.getMontoTotal(),
                java.util.List.of()
        );
        eventPublisher.publicar(evento);
    }
}
