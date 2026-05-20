package com.openlib.market.infrastructure.config;

import com.openlib.market.application.publicacion.IPublicarLibroUseCase;
import com.openlib.market.application.publicacion.PublicarLibroInteractor;
import com.openlib.market.application.publicacion.ISubirVistaPreviaUseCase;
import com.openlib.market.application.publicacion.SubirVistaPreviaInteractor;
import com.openlib.market.application.publicacion.IPublicarContenidoDigitalUseCase;
import com.openlib.market.application.publicacion.PublicarContenidoDigitalInteractor;
import com.openlib.market.domain.almacenamiento.IAlmacenamientoVistaPreviaGateway;
import com.openlib.market.domain.detalle.ILibroPublicacionGateway;
import com.openlib.market.domain.detalle.IContenidoDigitalGateway;
import com.openlib.market.domain.registro.IUsuarioGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PublicacionConfig {

    @Bean
    public IPublicarLibroUseCase publicarLibroUseCase(
            ILibroPublicacionGateway libroGateway,
            IUsuarioGateway usuarioGateway) {
        return new PublicarLibroInteractor(libroGateway, usuarioGateway);
    }

    @Bean
    public ISubirVistaPreviaUseCase subirVistaPreviaUseCase(
            ILibroPublicacionGateway libroGateway,
            IAlmacenamientoVistaPreviaGateway almacenamientoVistaPreviaGateway) {
        return new SubirVistaPreviaInteractor(libroGateway, almacenamientoVistaPreviaGateway);
    }

    @Bean
    public IPublicarContenidoDigitalUseCase publicarContenidoDigitalUseCase(
            IContenidoDigitalGateway contenidoDigitalGateway) {
        return new PublicarContenidoDigitalInteractor(contenidoDigitalGateway);
    }
}
