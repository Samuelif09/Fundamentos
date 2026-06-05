package com.openlib.market.domain.checkout;

import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.shared.AccionNoPermitidaException;

public class FallidoState implements PedidoState {
    @Override
    public void procesarPago(Pedido pedido) {
        throw new AccionNoPermitidaException("No se puede pagar un pedido que ya está fallido");
    }
}
