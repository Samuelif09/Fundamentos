package com.openlib.market.domain.checkout;

public interface ICheckoutEventPublisher {
    void publicar(CheckoutCompletadoEvent evento);
}
