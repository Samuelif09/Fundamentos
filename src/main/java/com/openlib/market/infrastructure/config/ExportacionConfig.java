package com.openlib.market.infrastructure.config;

import com.openlib.market.application.exportacion.ExportarMiCuentaInteractor;
import com.openlib.market.application.exportacion.IExportarMiCuentaUseCase;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.registro.IUsuarioGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExportacionConfig {

    @Bean
    public IExportarMiCuentaUseCase exportarMiCuentaUseCase(
            IUsuarioGateway usuarioGateway, 
            IPedidoGateway pedidoGateway) {
        return new ExportarMiCuentaInteractor(usuarioGateway, pedidoGateway);
    }
}
