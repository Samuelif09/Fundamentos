package com.openlib.market.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openlib.market.application.comparte.ICompartirComparteUseCase;
import com.openlib.market.application.comparte.CompartirComparteInteractor;
import com.openlib.market.domain.comparte.ILibroComparteGateway;

@Configuration
public class ComparteConfig {

    @Bean
    public ICompartirComparteUseCase compartirComparteUseCase(ILibroComparteGateway libroGateway) {
        return new CompartirComparteInteractor(libroGateway);
    }
}