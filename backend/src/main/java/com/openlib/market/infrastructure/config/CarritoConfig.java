package com.openlib.market.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openlib.market.application.carrito.IAgregarCarritoUseCase;
import com.openlib.market.application.carrito.AgregarCarritoInteractor;
import com.openlib.market.domain.carrito.ICarritoGateway;
import com.openlib.market.domain.carrito.ILibroGateway;
import com.openlib.market.domain.inventario.IInventarioGateway;

@Configuration
public class CarritoConfig {

    @Bean
    public IAgregarCarritoUseCase agregarCarritoUseCase(
            ICarritoGateway carritoGateway,
            ILibroGateway libroGateway,
            IInventarioGateway inventarioGateway
    ) {
        return new AgregarCarritoInteractor(carritoGateway, libroGateway, inventarioGateway);
    }

}