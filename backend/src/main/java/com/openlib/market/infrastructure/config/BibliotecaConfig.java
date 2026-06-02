package com.openlib.market.infrastructure.config;

import com.openlib.market.application.biblioteca.DescargarPostCompraInteractor;
import com.openlib.market.application.biblioteca.IDescargarPostCompraUseCase;
import com.openlib.market.domain.biblioteca.IAlmacenamientoGateway;
import com.openlib.market.domain.biblioteca.IBibliotecaGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BibliotecaConfig {

    @Bean
    public IDescargarPostCompraUseCase descargarPostCompraUseCase(
            IBibliotecaGateway bibliotecaGateway,
            IAlmacenamientoGateway almacenamientoGateway) {
        return new DescargarPostCompraInteractor(bibliotecaGateway, almacenamientoGateway);
    }

    @Bean
    public com.openlib.market.application.biblioteca.IVerBibliotecaUseCase verBibliotecaUseCase(
            IBibliotecaGateway bibliotecaGateway) {
        return new com.openlib.market.application.biblioteca.VerBibliotecaInteractor(bibliotecaGateway);
    }
}
