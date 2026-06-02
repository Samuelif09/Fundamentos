package com.openlib.market.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.openlib.market.application.curaduria.IRevisarCuraduriaContenidoUseCase;
import com.openlib.market.application.curaduria.RevisarCuraduriaContenidoInteractor;
import com.openlib.market.domain.curaduria.ICuraduriaGateway;
import com.openlib.market.domain.vendedor.IVendedorGateway;
import com.openlib.market.application.curaduria.IRechazarCuraduriaContenidoUseCase;
import com.openlib.market.application.curaduria.RechazarCuraduriaContenidoInteractor;
import com.openlib.market.application.curaduria.IAprobarCuraduriaContenidoUseCase;
import com.openlib.market.application.curaduria.AprobarCuraduriaContenidoInteractor;
import com.openlib.market.domain.notificacion.INotificacionGateway;
@Configuration
public class CuraduriaConfig {

    @Bean
    public IRevisarCuraduriaContenidoUseCase revisarCuraduriaContenidoUseCase(
            ICuraduriaGateway curaduriaGateway,
            IVendedorGateway vendedorGateway) {

        return new RevisarCuraduriaContenidoInteractor(curaduriaGateway, vendedorGateway);
    }

    @Bean
    public IRechazarCuraduriaContenidoUseCase rechazarCuraduriaContenidoUseCase(
            ICuraduriaGateway curaduriaGateway,
            INotificacionGateway notificacionGateway) {

        return new RechazarCuraduriaContenidoInteractor(curaduriaGateway, notificacionGateway);
    }

    @Bean
    public IAprobarCuraduriaContenidoUseCase aprobarCuraduriaContenidoUseCase(
            ICuraduriaGateway curaduriaGateway) {
        return new AprobarCuraduriaContenidoInteractor(curaduriaGateway);
    }
}