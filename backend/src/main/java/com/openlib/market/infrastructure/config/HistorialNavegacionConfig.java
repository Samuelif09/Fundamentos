package com.openlib.market.infrastructure.config;

import com.openlib.market.application.historial.IVerHistorialNavegacionUseCase;
import com.openlib.market.application.historial.RegistrarVistaLibroInteractor;
import com.openlib.market.application.historial.VerHistorialNavegacionInteractor;
import com.openlib.market.domain.detalle.IDetalleGateway;
import com.openlib.market.domain.historial.IHistorialNavegacionGateway;
import com.openlib.market.domain.historial.LibroVistoEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

@Configuration
public class HistorialNavegacionConfig {

    @Bean
    public IVerHistorialNavegacionUseCase verHistorialNavegacionUseCase(
            IHistorialNavegacionGateway historialGateway,
            IDetalleGateway detalleGateway) {
        return new VerHistorialNavegacionInteractor(historialGateway, detalleGateway);
    }

    @Bean
    public RegistrarVistaLibroInteractor registrarVistaLibroInteractor(
            IHistorialNavegacionGateway historialGateway) {
        return new RegistrarVistaLibroInteractor(historialGateway);
    }

    // Adaptador de Spring Events al UseCase
    @Bean
    public HistorialEventListener historialEventListener(RegistrarVistaLibroInteractor interactor) {
        return new HistorialEventListener(interactor);
    }

    public static class HistorialEventListener {
        private final RegistrarVistaLibroInteractor interactor;

        public HistorialEventListener(RegistrarVistaLibroInteractor interactor) {
            this.interactor = interactor;
        }

        @Async
        @EventListener
        public void onLibroVistoEvent(LibroVistoEvent event) {
            interactor.manejarLibroVisto(event);
        }
    }
}
