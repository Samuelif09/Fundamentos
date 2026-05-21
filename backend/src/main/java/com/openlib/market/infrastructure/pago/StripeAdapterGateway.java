package com.openlib.market.infrastructure.pago;

import com.openlib.market.domain.pago.IPagoExternoGateway;
import com.openlib.market.domain.pago.MetodoPago;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class StripeAdapterGateway implements IPagoExternoGateway {
    private static final Logger LOGGER = Logger.getLogger(StripeAdapterGateway.class.getName());

    @Override
    public boolean procesar(double monto, MetodoPago metodoPago) {
        LOGGER.info(String.format("Stripe procesando pago de TARJETA por monto: %.2f", monto));
        
        if (metodoPago.getDetalle().endsWith("0000")) {
            LOGGER.warning("Stripe: Tarjeta rechazada simulada");
            return false;
        }
        return true;
    }
}
