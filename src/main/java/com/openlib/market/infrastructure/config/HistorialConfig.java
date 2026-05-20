package com.openlib.market.infrastructure.config;

import com.openlib.market.application.pago.IVerMiCuentaUseCase;
import com.openlib.market.application.pago.VerMiCuentaInteractor;
import com.openlib.market.domain.pago.IPedidoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HistorialConfig {

    @Bean
    public IVerMiCuentaUseCase verMiCuentaUseCase(IPedidoGateway pedidoGateway) {
        return new VerMiCuentaInteractor(pedidoGateway);
    }
}
