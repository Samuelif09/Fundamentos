package com.openlib.market.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.openlib.market.application.gestionUsuarios.IAprobarGestionUsuariosUseCase;
import com.openlib.market.application.gestionUsuarios.AprobarGestionUsuariosInteractor;
import com.openlib.market.domain.vendedor.IVendedorGateway;
import com.openlib.market.domain.vendedor.INotificacionAdminGateway;

@Configuration
public class GestionUsuariosConfig {

    @Bean
    public IAprobarGestionUsuariosUseCase aprobarGestionUsuariosUseCase(
            IVendedorGateway vendedorGateway,
            INotificacionAdminGateway notificacionGateway
    ) {
        return new AprobarGestionUsuariosInteractor(vendedorGateway, notificacionGateway);
    }
}