package com.openlib.market.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openlib.market.application.explorar.IExplorarBusquedaUseCase;
import com.openlib.market.application.explorar.ExplorarBusquedaInteractor;
import com.openlib.market.domain.explorar.ITendenciaGateway;

@Configuration
public class ExplorarConfig {

    @Bean
    public IExplorarBusquedaUseCase explorarBusquedaUseCase(ITendenciaGateway tendenciaGateway) {
        return new ExplorarBusquedaInteractor(tendenciaGateway);
    }

}