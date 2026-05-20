package com.openlib.market.infrastructure.config;

import com.openlib.market.application.tienda.ITenerMiTiendaUseCase;
import com.openlib.market.application.tienda.TenerMiTiendaInteractor;
import com.openlib.market.application.tienda.IPersonalizarMiTiendaUseCase;
import com.openlib.market.application.tienda.PersonalizarMiTiendaInteractor;
import com.openlib.market.domain.almacenamiento.IAlmacenamientoGateway;
import com.openlib.market.domain.catalogo.IInventarioGateway;
import com.openlib.market.domain.tienda.ITiendaVendedorGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TiendaConfig {

    @Bean
    public ITenerMiTiendaUseCase tenerMiTiendaUseCase(
            ITiendaVendedorGateway tiendaVendedorGateway,
            IInventarioGateway inventarioGateway) {
        return new TenerMiTiendaInteractor(tiendaVendedorGateway, inventarioGateway);
    }

    @Bean
    public IPersonalizarMiTiendaUseCase personalizarMiTiendaUseCase(
            IAlmacenamientoGateway almacenamientoGateway,
            ITiendaVendedorGateway tiendaVendedorGateway) {
        return new PersonalizarMiTiendaInteractor(almacenamientoGateway, tiendaVendedorGateway);
    }
}
