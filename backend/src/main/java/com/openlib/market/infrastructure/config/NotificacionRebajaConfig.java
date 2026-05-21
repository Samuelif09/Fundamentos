package com.openlib.market.infrastructure.config;

import com.openlib.market.application.notificacion.RecibirNotificacionPrecioInteractor;
import com.openlib.market.domain.listadeseos.IListaDeseosGateway;
import com.openlib.market.domain.notificacionrebaja.INotificacionGateway;
import com.openlib.market.domain.notificacionrebaja.LibroCambioPrecioEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@Configuration
public class NotificacionRebajaConfig {

    @Bean
    public RecibirNotificacionPrecioInteractor recibirNotificacionPrecioInteractor(
            IListaDeseosGateway listaDeseosGateway,
            INotificacionGateway notificacionGateway) {
        return new RecibirNotificacionPrecioInteractor(listaDeseosGateway, notificacionGateway);
    }

    @Bean
    public NotificacionPrecioEventListener notificacionPrecioEventListener(
            RecibirNotificacionPrecioInteractor interactor) {
        return new NotificacionPrecioEventListener(interactor);
    }

    public static class NotificacionPrecioEventListener {
        private final RecibirNotificacionPrecioInteractor interactor;

        public NotificacionPrecioEventListener(RecibirNotificacionPrecioInteractor interactor) {
            this.interactor = interactor;
        }

        @Async
        @EventListener
        public void onLibroCambioPrecioEvent(LibroCambioPrecioEvent event) {
            interactor.manejarCambioPrecio(event);
        }
    }
}
