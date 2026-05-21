package com.openlib.market.infrastructure.config;

import com.openlib.market.application.inventario.ActualizarInventarioInteractor;
import com.openlib.market.application.inventario.IActualizarInventarioUseCase;
import com.openlib.market.domain.detalle.IActualizarLibroGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActualizarInventarioConfig {

    @Bean
    public IActualizarInventarioUseCase actualizarInventarioUseCase(IActualizarLibroGateway libroGateway) {
        return new ActualizarInventarioInteractor(libroGateway);
    }
}
