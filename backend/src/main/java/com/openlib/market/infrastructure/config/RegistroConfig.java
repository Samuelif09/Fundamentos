package com.openlib.market.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openlib.market.application.registro.IRegistrarRegistroUseCase;
import com.openlib.market.application.registro.RegistrarRegistroInteractor;
import com.openlib.market.domain.registro.IRegistroGateway;
import com.openlib.market.domain.registro.IPasswordEncoderGateway;

@Configuration
public class RegistroConfig {

    @Bean
    public IRegistrarRegistroUseCase registrarRegistroUseCase(
            IRegistroGateway registroGateway,
            IPasswordEncoderGateway passwordEncoder
    ) {
        return new RegistrarRegistroInteractor(registroGateway, passwordEncoder);
    }

}