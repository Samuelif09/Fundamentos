package com.openlib.market.infrastructure.config;

import com.openlib.market.application.autenticacion.*;
import com.openlib.market.domain.autenticacion.*;
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

    @Bean
    public IIniciarAutenticacionUseCase iniciarAutenticacionUseCase(
            IUsuarioAuthGateway usuarioGateway,
            IVerificadorPasswordGateway verificadorPassword,
            ITokenGeneratorGateway tokenGenerator) {

        return new IniciarAutenticacionInteractor(usuarioGateway, verificadorPassword, tokenGenerator);
    }

    @Bean
    public IGestionarRolesUseCase gestionarRolesUseCase(IAdminGateway adminGateway) {
        return new GestionarRolesInteractor(adminGateway);
    }
}
