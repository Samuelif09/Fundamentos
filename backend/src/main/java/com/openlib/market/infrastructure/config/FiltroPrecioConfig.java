package com.openlib.market.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openlib.market.application.filtroprecio.IFiltrarPorPrecioUseCase;
import com.openlib.market.application.filtroprecio.FiltrarPorPrecioInteractor;
import com.openlib.market.domain.filtroprecio.IFiltroPrecioGateway;

@Configuration
public class FiltroPrecioConfig {

    @Bean
    public IFiltrarPorPrecioUseCase filtrarPorPrecioUseCase(IFiltroPrecioGateway filtroPrecioGateway) {
        return new FiltrarPorPrecioInteractor(filtroPrecioGateway);
    }

}