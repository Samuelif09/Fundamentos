package com.openlib.market.infrastructure.config;

import com.openlib.market.application.afiliado.ConfigurarAfiliadosInteractor;
import com.openlib.market.application.afiliado.IConfigurarAfiliadosUseCase;
import com.openlib.market.domain.afiliado.IAfiliadoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AfiliadoConfig {

    @Bean
    public IConfigurarAfiliadosUseCase configurarAfiliadosUseCase(IAfiliadoGateway afiliadoGateway) {
        return new ConfigurarAfiliadosInteractor(afiliadoGateway);
    }
}
