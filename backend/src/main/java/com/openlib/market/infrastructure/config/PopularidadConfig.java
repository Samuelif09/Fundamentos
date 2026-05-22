package com.openlib.market.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openlib.market.application.popularidad.IFiltrarPopularidadUseCase;
import com.openlib.market.application.popularidad.FiltrarPopularidadInteractor;
import com.openlib.market.domain.popularidad.IPopularidadGateway;

@Configuration
public class PopularidadConfig {

    @Bean
    public IFiltrarPopularidadUseCase filtrarPopularidadUseCase(IPopularidadGateway popularidadGateway) {
        return new FiltrarPopularidadInteractor(popularidadGateway);
    }

}