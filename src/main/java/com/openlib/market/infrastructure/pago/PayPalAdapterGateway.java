package com.openlib.market.infrastructure.pago;

import com.openlib.market.domain.pago.IPagoExternoGateway;
import com.openlib.market.domain.pago.MetodoPago;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class PayPalAdapterGateway implements IPagoExternoGateway {
    private static final Logger LOGGER = Logger.getLogger(PayPalAdapterGateway.class.getName());

    @Override
    public boolean procesar(double monto, MetodoPago metodoPago) {
        LOGGER.info(String.format("PayPal procesando pago por monto: %.2f", monto));
        
        if (metodoPago.getDetalle().contains("error")) {
            LOGGER.warning("PayPal: Pago rechazado simulado");
            return false;
        }
        return true;
    }
}
