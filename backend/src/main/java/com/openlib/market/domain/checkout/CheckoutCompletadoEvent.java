package com.openlib.market.domain.checkout;

public class CheckoutCompletadoEvent {
    private final String pedidoId;
    private final String sesionId;

    public CheckoutCompletadoEvent(String pedidoId, String sesionId) {
        this.pedidoId = pedidoId;
        this.sesionId = sesionId;
    }

    public String getPedidoId() {
        return pedidoId;
    }

    public String getSesionId() {
        return sesionId;
    }
}
