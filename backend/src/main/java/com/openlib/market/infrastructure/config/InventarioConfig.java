package com.openlib.market.infrastructure.config;

import com.openlib.market.application.inventario.IVerInventarioUseCase;
import com.openlib.market.application.inventario.VerInventarioInteractor;
import com.openlib.market.application.inventario.IDespublicarInventarioUseCase;
import com.openlib.market.application.inventario.DespublicarInventarioInteractor;
import com.openlib.market.application.inventario.ICrearDescuentoInventarioUseCase;
import com.openlib.market.application.inventario.CrearDescuentoInventarioInteractor;
import com.openlib.market.domain.catalogo.IInventarioGateway;
import com.openlib.market.domain.detalle.ILibroPublicacionGateway;
import com.openlib.market.domain.inventario.IPromocionGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventarioConfig {

    @Bean
    public IVerInventarioUseCase verInventarioUseCase(IInventarioGateway inventarioGateway) {
        return new VerInventarioInteractor(inventarioGateway);
    }

    @Bean
    public IDespublicarInventarioUseCase despublicarInventarioUseCase(ILibroPublicacionGateway libroPublicacionGateway) {
        return new DespublicarInventarioInteractor(libroPublicacionGateway);
    }

    @Bean
    public ICrearDescuentoInventarioUseCase crearDescuentoInventarioUseCase(
            ILibroPublicacionGateway libroPublicacionGateway,
            IPromocionGateway promocionGateway) {
        return new CrearDescuentoInventarioInteractor(libroPublicacionGateway, promocionGateway);
    }
}
