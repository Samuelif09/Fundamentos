package com.openlib.market.infrastructure.config;

import com.openlib.market.application.ventavendedor.IRecibirVentasUseCase;
import com.openlib.market.application.ventavendedor.RecibirVentasInteractor;
import com.openlib.market.domain.ventavendedor.IDetalleLibroGateway;
import com.openlib.market.domain.ventavendedor.INotificacionVendedorGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class VentaVendedorConfig {

    @Bean
    public IRecibirVentasUseCase recibirVentasUseCase(
            IDetalleLibroGateway detalleLibroGateway,
            INotificacionVendedorGateway notificacionGateway) {
        return new RecibirVentasInteractor(detalleLibroGateway, notificacionGateway);
    }
}
