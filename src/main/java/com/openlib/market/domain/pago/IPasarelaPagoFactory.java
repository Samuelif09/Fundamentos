package com.openlib.market.domain.pago;

public interface IPasarelaPagoFactory {
    IPagoExternoGateway obtenerPasarela(TipoMetodoPago tipo);
}
