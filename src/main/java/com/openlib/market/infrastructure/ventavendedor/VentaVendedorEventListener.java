package com.openlib.market.infrastructure.ventavendedor;

import com.openlib.market.application.ventavendedor.IRecibirVentasUseCase;
import com.openlib.market.domain.pago.PedidoCompletadoEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class VentaVendedorEventListener {

    private final IRecibirVentasUseCase recibirVentasUseCase;

    public VentaVendedorEventListener(IRecibirVentasUseCase recibirVentasUseCase) {
        this.recibirVentasUseCase = recibirVentasUseCase;
    }

    @Async
    @EventListener
    public void handlePedidoCompletado(PedidoCompletadoEvent event) {
        recibirVentasUseCase.onPedidoCompletado(event);
    }
}
