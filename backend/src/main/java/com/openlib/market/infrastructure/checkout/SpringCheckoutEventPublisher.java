package com.openlib.market.infrastructure.checkout;

import com.openlib.market.domain.checkout.CheckoutCompletadoEvent;
import com.openlib.market.domain.checkout.ICheckoutEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringCheckoutEventPublisher implements ICheckoutEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringCheckoutEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publicar(CheckoutCompletadoEvent evento) {
        applicationEventPublisher.publishEvent(evento);
    }
}
