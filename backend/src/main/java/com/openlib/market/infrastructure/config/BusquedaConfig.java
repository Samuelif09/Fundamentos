package com.openlib.market.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openlib.market.application.busqueda.IBuscarBusquedaUseCase;
import com.openlib.market.application.busqueda.BuscarBusquedaInteractor;
import com.openlib.market.domain.busqueda.IBusquedaGateway;

@Configuration
public class BusquedaConfig {

    @Bean
    public IBuscarBusquedaUseCase buscarBusquedaUseCase(IBusquedaGateway busquedaGateway) {
        return new BuscarBusquedaInteractor(busquedaGateway);
    }
}