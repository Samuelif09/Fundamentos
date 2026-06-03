package com.openlib.market.domain.checkout;

import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.shared.AccionNoPermitidaException;

public class PagadoState implements PedidoState {
    @Override
    public void procesarPago(Pedido pedido) {
        throw new AccionNoPermitidaException("El pedido ya se encuentra pagado");
    }
}
