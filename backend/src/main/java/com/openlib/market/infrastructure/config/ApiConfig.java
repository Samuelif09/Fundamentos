package com.openlib.market.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.openlib.market.application.api.IGenerarCredencialesApiUseCase;
import com.openlib.market.application.api.GenerarCredencialesApiInteractor;
import com.openlib.market.domain.api.IApiKeyGateway;

@Configuration
public class ApiConfig {

    @Bean
    public IGenerarCredencialesApiUseCase generarCredencialesApiUseCase(IApiKeyGateway apiKeyGateway) {
        return new GenerarCredencialesApiInteractor(apiKeyGateway);
    }

}