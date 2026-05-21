package com.openlib.market.infrastructure.config;

import com.openlib.market.application.cupon.AplicarCuponInteractor;
import com.openlib.market.application.cupon.IAplicarCuponUseCase;
import com.openlib.market.domain.carrito.ICarritoGateway;
import com.openlib.market.domain.cupon.ICuponGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CuponConfig {

    @Bean
    public IAplicarCuponUseCase aplicarCuponUseCase(ICuponGateway cuponGateway, ICarritoGateway carritoGateway) {
        return new AplicarCuponInteractor(cuponGateway, carritoGateway);
    }
}
