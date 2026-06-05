package com.openlib.market.infrastructure.checkout;

import com.openlib.market.domain.checkout.IPasarelaPagoSimuladaGateway;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PasarelaPagoSimuladaAdapter implements IPasarelaPagoSimuladaGateway {
    @Override
    public String procesarPago(double monto) {
        // Simulamos un retraso o lógica de pago
        return UUID.randomUUID().toString();
    }
}
