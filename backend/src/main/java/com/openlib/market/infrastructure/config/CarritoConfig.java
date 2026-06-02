package com.openlib.market.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openlib.market.application.carrito.IAgregarCarritoUseCase;
import com.openlib.market.application.carrito.AgregarCarritoInteractor;
import com.openlib.market.domain.carrito.ICarritoGateway;
import com.openlib.market.domain.carrito.ILibroGateway;
import com.openlib.market.domain.inventario.IInventarioGateway;

import com.openlib.market.domain.detalle.IContenidoDigitalGateway;

@Configuration
public class CarritoConfig {

    @Bean
    public IAgregarCarritoUseCase agregarCarritoUseCase(
            ICarritoGateway carritoGateway,
            ILibroGateway libroGateway,
            IInventarioGateway inventarioGateway,
            IContenidoDigitalGateway contenidoGateway
    ) {
        return new AgregarCarritoInteractor(carritoGateway, libroGateway, inventarioGateway, contenidoGateway);
    }

}