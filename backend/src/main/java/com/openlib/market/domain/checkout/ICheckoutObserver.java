package com.openlib.market.domain.checkout;

public interface ICheckoutObserver {
    void onCheckoutCompletado(CheckoutCompletadoEvent event);
}
