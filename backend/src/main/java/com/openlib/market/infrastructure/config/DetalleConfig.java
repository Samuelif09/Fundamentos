package com.openlib.market.infrastructure.config;

import com.openlib.market.application.detalle.IVerDetalleLibroUseCase;
import com.openlib.market.application.detalle.IVerDetalleUseCase;
import com.openlib.market.application.detalle.VerDetalleInteractor;
import com.openlib.market.application.detalle.VerDetalleLibroInteractor;
import com.openlib.market.domain.detalle.IDetalleGateway;
import com.openlib.market.domain.inventario.IInventarioGateway;
import com.openlib.market.domain.inventario.IPromocionGateway;
import com.openlib.market.domain.shared.IEventPublisher;
import com.openlib.market.domain.registro.IUsuarioGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DetalleConfig {

    @Bean
    public IVerDetalleLibroUseCase verDetalleLibroUseCase(
            IDetalleGateway detalleGateway, 
            IInventarioGateway inventarioGateway,
            @Autowired(required = false) IEventPublisher eventPublisher,
            @Autowired(required = false) IPromocionGateway promocionGateway,
            @Autowired(required = false) IUsuarioGateway usuarioGateway) {
        return new VerDetalleLibroInteractor(detalleGateway, inventarioGateway, eventPublisher, promocionGateway, usuarioGateway);
    }

    @Bean
    public IVerDetalleUseCase verDetalleUseCase(IDetalleGateway detalleGateway) {
        return new VerDetalleInteractor(detalleGateway);
    }
}
