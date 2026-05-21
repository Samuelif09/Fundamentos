package com.openlib.market.infrastructure.config;

import com.openlib.market.application.catalogo.*;
import com.openlib.market.application.catalogo.*;
import com.openlib.market.domain.catalogo.ICatalogoGateway;
import com.openlib.market.domain.catalogo.IReglaPricingGateway;
import com.openlib.market.domain.detalle.IDetalleGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogoConfig {

    @Bean
    public IVerCatalogoUseCase verCatalogoUseCase(ICatalogoGateway catalogoGateway) {
        return new VerCatalogoInteractor(catalogoGateway);
    }

    @Bean
    public IBuscarCatalogoUseCase buscarCatalogoUseCase(ICatalogoGateway catalogoGateway) {
        return new BuscarCatalogoInteractor(catalogoGateway);
    }

    @Bean
    public IVerLibrosRelacionadosUseCase verLibrosRelacionadosUseCase(ICatalogoGateway catalogoGateway,
                                                                       IDetalleGateway detalleGateway) {
        return new VerLibrosRelacionadosInteractor(catalogoGateway, detalleGateway);
    }

    @Bean
    public IConfigurarPricingDinamicoUseCase configurarPricingDinamicoUseCase(IReglaPricingGateway reglaPricingGateway) {
        return new ConfigurarPricingDinamicoInteractor(reglaPricingGateway);
    }
}
