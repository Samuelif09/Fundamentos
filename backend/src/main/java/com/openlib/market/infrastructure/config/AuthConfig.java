package com.openlib.market.infrastructure.config;

import com.openlib.market.application.autenticacion.IRecuperarAutenticacionUseCase;
import com.openlib.market.application.autenticacion.RecuperarAutenticacionInteractor;
import com.openlib.market.domain.autenticacion.IEmailGateway;
import com.openlib.market.domain.autenticacion.ITokenRecuperacionGateway;
import com.openlib.market.domain.autenticacion.IUsuarioAuthGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    public IRecuperarAutenticacionUseCase recuperarAutenticacionUseCase(
            IUsuarioAuthGateway usuarioGateway,
            ITokenRecuperacionGateway tokenGateway,
            IEmailGateway emailGateway) {
        return new RecuperarAutenticacionInteractor(usuarioGateway, tokenGateway, emailGateway);
    }
}
