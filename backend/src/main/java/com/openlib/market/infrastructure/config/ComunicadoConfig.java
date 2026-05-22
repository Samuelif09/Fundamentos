package com.openlib.market.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openlib.market.application.comunicado.IEnviarComunicadoUseCase;
import com.openlib.market.application.comunicado.EnviarComunicadoInteractor;
import com.openlib.market.domain.registro.IUsuarioGateway;
import com.openlib.market.domain.comunicado.INotificacionGateway;

@Configuration
public class ComunicadoConfig {

    @Bean
    public IEnviarComunicadoUseCase enviarComunicadoUseCase(
            IUsuarioGateway usuarioGateway,
            INotificacionGateway notificacionGateway
    ) {
        return new EnviarComunicadoInteractor(usuarioGateway, notificacionGateway);
    }
}