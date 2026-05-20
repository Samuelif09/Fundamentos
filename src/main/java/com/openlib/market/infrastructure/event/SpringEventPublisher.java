package com.openlib.market.infrastructure.event;

import com.openlib.market.domain.shared.IEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringEventPublisher implements IEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publicar(Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}
