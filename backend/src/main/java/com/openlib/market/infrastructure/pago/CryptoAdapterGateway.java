package com.openlib.market.infrastructure.pago;

import com.openlib.market.domain.pago.IPagoExternoGateway;
import com.openlib.market.domain.pago.MetodoPago;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class CryptoAdapterGateway implements IPagoExternoGateway {
    private static final Logger LOGGER = Logger.getLogger(CryptoAdapterGateway.class.getName());

    @Override
    public boolean procesar(double monto, MetodoPago metodoPago) {
        LOGGER.info(String.format("Procesando pago con Criptomonedas (Wallet: %s) por monto: %.2f", metodoPago.getDetalle(), monto));
        return true; // Simulación siempre exitosa para crypto por defecto
    }
}
