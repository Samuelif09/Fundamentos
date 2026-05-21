package com.openlib.market.infrastructure.pago;

import com.openlib.market.domain.pago.IPasarelaPagoGateway;
import com.openlib.market.domain.pago.Monto;
import com.openlib.market.domain.pago.TokenPago;
import org.springframework.stereotype.Component;

@Component
public class PasarelaPagoDummyGateway implements IPasarelaPagoGateway {

    @Override
    public boolean procesarCobro(TokenPago token, Monto monto) {
        // Dummy: Si el token termina en "par" o simplemente si no contiene "fail", aprobamos
        String v = token.getValor().toLowerCase();
        if (v.contains("fail") || v.contains("error")) {
            return false;
        }
        return true;
    }
}
