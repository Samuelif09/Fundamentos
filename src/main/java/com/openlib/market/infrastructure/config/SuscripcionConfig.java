package com.openlib.market.infrastructure.config;

import com.openlib.market.application.suscripcion.ISeguirMiCuentaUseCase;
import com.openlib.market.application.suscripcion.SeguirMiCuentaInteractor;
import com.openlib.market.domain.registro.IUsuarioGateway;
import com.openlib.market.domain.suscripcion.ISuscripcionGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SuscripcionConfig {

    @Bean
    public ISeguirMiCuentaUseCase seguirMiCuentaUseCase(
            ISuscripcionGateway suscripcionGateway, 
            IUsuarioGateway usuarioGateway) {
        return new SeguirMiCuentaInteractor(suscripcionGateway, usuarioGateway);
    }
}
