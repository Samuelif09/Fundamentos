package com.openlib.market.application.ventavendedor;

import com.openlib.market.domain.pago.PedidoCompletadoEvent;

public interface IRecibirVentasUseCase {
    void onPedidoCompletado(PedidoCompletadoEvent event);
}
