package com.openlib.market.application.checkout;

import com.openlib.market.domain.carrito.CarritoCompras;
import com.openlib.market.domain.carrito.ICarritoGateway;
import com.openlib.market.domain.carrito.SesionId;
import com.openlib.market.domain.checkout.CheckoutCompletadoEvent;
import com.openlib.market.domain.checkout.ICheckoutObserver;

public class CarritoCheckoutObserver implements ICheckoutObserver {

    private final ICarritoGateway carritoGateway;

    public CarritoCheckoutObserver(ICarritoGateway carritoGateway) {
        this.carritoGateway = carritoGateway;
    }

    @Override
    public void onCheckoutCompletado(CheckoutCompletadoEvent event) {
        carritoGateway.eliminarPorSesionId(new SesionId(event.getSesionId()));
    }
}
