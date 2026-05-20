package com.openlib.market.domain.shared;

public interface IEventPublisher {
    void publicar(Object event);
}
