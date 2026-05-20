package com.openlib.market.infrastructure.config;

import com.openlib.market.application.almacenamiento.ISubirImagenLibroUseCase;
import com.openlib.market.application.almacenamiento.SubirImagenLibroInteractor;
import com.openlib.market.domain.almacenamiento.IAlmacenamientoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlmacenamientoConfig {

    @Bean
    public ISubirImagenLibroUseCase subirImagenLibroUseCase(IAlmacenamientoGateway almacenamientoGateway) {
        return new SubirImagenLibroInteractor(almacenamientoGateway);
    }
}
