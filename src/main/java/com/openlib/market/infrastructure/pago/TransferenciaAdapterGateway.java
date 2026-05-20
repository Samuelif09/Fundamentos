package com.openlib.market.infrastructure.pago;

import com.openlib.market.domain.pago.IPagoExternoGateway;
import com.openlib.market.domain.pago.MetodoPago;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class TransferenciaAdapterGateway implements IPagoExternoGateway {
    private static final Logger LOGGER = Logger.getLogger(TransferenciaAdapterGateway.class.getName());

    @Override
    public boolean procesar(double monto, MetodoPago metodoPago) {
        LOGGER.info(String.format("Validando comprobante de transferencia bancaria (%s) por monto: %.2f", metodoPago.getDetalle(), monto));
        return true; 
    }
}
