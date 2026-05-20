package com.openlib.market.infrastructure.pago;

import com.openlib.market.domain.pago.IEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringEventPublisherAdapter implements IEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringEventPublisherAdapter(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publicar(Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}
