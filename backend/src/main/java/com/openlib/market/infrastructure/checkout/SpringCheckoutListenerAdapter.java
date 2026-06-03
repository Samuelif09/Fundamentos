package com.openlib.market.infrastructure.checkout;

import com.openlib.market.application.checkout.CarritoCheckoutObserver;
import com.openlib.market.application.checkout.InventarioCheckoutObserver;
import com.openlib.market.domain.checkout.CheckoutCompletadoEvent;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;

@Component
public class SpringCheckoutListenerAdapter {

    private final CarritoCheckoutObserver carritoObserver;
    private final InventarioCheckoutObserver inventarioObserver;

    public SpringCheckoutListenerAdapter(CarritoCheckoutObserver carritoObserver, InventarioCheckoutObserver inventarioObserver) {
        this.carritoObserver = carritoObserver;
        this.inventarioObserver = inventarioObserver;
    }

    @EventListener
    public void onCheckoutCompletado(CheckoutCompletadoEvent evento) {
        // Ejecutamos los observers de dominio
        carritoObserver.onCheckoutCompletado(evento);
        inventarioObserver.onCheckoutCompletado(evento);
    }
}
