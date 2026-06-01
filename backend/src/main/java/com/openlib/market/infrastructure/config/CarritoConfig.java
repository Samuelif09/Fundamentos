package com.openlib.market.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openlib.market.application.carrito.*;
import com.openlib.market.domain.carrito.ICarritoGateway;
import com.openlib.market.domain.carrito.ILibroGateway;
import com.openlib.market.domain.inventario.IInventarioGateway;
import com.openlib.market.domain.detalle.IDetalleGateway;
import com.openlib.market.domain.registro.IUsuarioGateway;

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

    @Bean
    public IVerCarritoUseCase verCarritoUseCase(
            ICarritoGateway carritoGateway,
            IDetalleGateway detalleGateway,
            IUsuarioGateway usuarioGateway
    ) {
        return new VerCarritoInteractor(carritoGateway, detalleGateway, usuarioGateway);
    }

    @Bean
    public IActualizarItemCarritoUseCase actualizarItemCarritoUseCase(
            ICarritoGateway carritoGateway,
            IInventarioGateway inventarioGateway
    ) {
        return new ActualizarItemCarritoInteractor(carritoGateway, inventarioGateway);
    }
}