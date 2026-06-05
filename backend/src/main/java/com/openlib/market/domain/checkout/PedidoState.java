package com.openlib.market.domain.checkout;

import com.openlib.market.domain.pago.Pedido;

public interface PedidoState {
    void procesarPago(Pedido pedido);
}
