package com.openlib.market.domain.pago;

public interface IPasarelaPagoGateway {
    boolean procesarCobro(TokenPago token, Monto monto);
}
