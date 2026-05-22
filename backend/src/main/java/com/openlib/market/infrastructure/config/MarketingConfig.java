package com.openlib.market.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openlib.market.application.marketing.IGestionarBannersUseCase;
import com.openlib.market.application.marketing.GestionarBannersInteractor;
import com.openlib.market.domain.marketing.IBannerGateway;

@Configuration
public class MarketingConfig {

    @Bean
    public IGestionarBannersUseCase gestionarBannersUseCase(IBannerGateway bannerGateway) {
        return new GestionarBannersInteractor(bannerGateway);
    }
}