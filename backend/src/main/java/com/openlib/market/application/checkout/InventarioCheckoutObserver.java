package com.openlib.market.application.checkout;

import com.openlib.market.domain.checkout.CheckoutCompletadoEvent;
import com.openlib.market.domain.checkout.ICheckoutObserver;
import com.openlib.market.domain.detalle.IContenidoDigitalGateway;
import com.openlib.market.domain.inventario.IInventarioGateway;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.pago.ItemPedido;

public class InventarioCheckoutObserver implements ICheckoutObserver {

    private final IPedidoGateway pedidoGateway;
    private final IInventarioGateway inventarioGateway;
    private final IContenidoDigitalGateway contenidoGateway;

    public InventarioCheckoutObserver(IPedidoGateway pedidoGateway, IInventarioGateway inventarioGateway, IContenidoDigitalGateway contenidoGateway) {
        this.pedidoGateway = pedidoGateway;
        this.inventarioGateway = inventarioGateway;
        this.contenidoGateway = contenidoGateway;
    }

    @Override
    public void onCheckoutCompletado(CheckoutCompletadoEvent event) {
        pedidoGateway.obtenerPorId(event.getPedidoId()).ifPresent(pedido -> {
            for (ItemPedido item : pedido.getItems()) {
                contenidoGateway.obtenerContenidoPorId(item.getIsbn()).ifPresent(contenido -> {
                    if (contenido.requiereControlDeInventario()) {
                        // Descontar stock
                        inventarioGateway.restarStock(item.getIsbn(), item.getCantidad());
                    }
                });
            }
        });
    }
}
