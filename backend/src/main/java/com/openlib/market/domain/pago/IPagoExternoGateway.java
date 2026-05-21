package com.openlib.market.domain.pago;

public interface IPagoExternoGateway {
    boolean procesar(double monto, MetodoPago metodoPago);
}
