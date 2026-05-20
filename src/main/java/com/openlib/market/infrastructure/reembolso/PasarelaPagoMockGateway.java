package com.openlib.market.infrastructure.reembolso;

import com.openlib.market.domain.reembolso.IPasarelaPagoGateway;
import org.springframework.stereotype.Component;

@Component
public class PasarelaPagoMockGateway implements IPasarelaPagoGateway {

    @Override
    public boolean ejecutarReembolso(String idPedido, double monto) {
        System.out.println(">>> MOCK: Ejecutando reembolso en pasarela externa");
        System.out.println(">>> Pedido: " + idPedido + " | Monto a devolver: $" + monto);
        System.out.println(">>> Resultado: SUCCESS");
        return true; // Simular que siempre tiene éxito
    }
}
