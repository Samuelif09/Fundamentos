package com.openlib.market.domain.checkout;

import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.pago.EstadoPedido;

public class PendientePagoState implements PedidoState {
    @Override
    public void procesarPago(Pedido pedido) {
        pedido.setEstado(EstadoPedido.PAGADO);
        pedido.setPedidoState(new PagadoState());
    }
}
