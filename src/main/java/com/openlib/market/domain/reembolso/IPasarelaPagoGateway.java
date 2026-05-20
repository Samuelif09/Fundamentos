package com.openlib.market.domain.reembolso;

public interface IPasarelaPagoGateway {
    boolean ejecutarReembolso(String idPedido, double monto);
}
