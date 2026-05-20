package com.openlib.market.infrastructure.config;

import com.openlib.market.application.vendedor.IRegistrarVendedorUseCase;
import com.openlib.market.application.vendedor.RegistrarVendedorInteractor;
import com.openlib.market.application.vendedor.IVerificarRegistroVendedorUseCase;
import com.openlib.market.application.vendedor.VerificarRegistroVendedorInteractor;
import com.openlib.market.domain.almacenamiento.IAlmacenamientoGateway;
import com.openlib.market.domain.registro.IPasswordEncoderGateway;
import com.openlib.market.domain.registro.IRegistroGateway;
import com.openlib.market.domain.vendedor.INotificacionAdminGateway;
import com.openlib.market.domain.vendedor.IVendedorGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VendedorConfig {

    @Bean
    public IRegistrarVendedorUseCase registrarVendedorUseCase(
            IRegistroGateway registroGateway,
            IVendedorGateway vendedorGateway,
            IPasswordEncoderGateway passwordEncoder) {
        return new RegistrarVendedorInteractor(registroGateway, vendedorGateway, passwordEncoder);
    }

    @Bean
    public IVerificarRegistroVendedorUseCase verificarRegistroVendedorUseCase(
            IVendedorGateway vendedorGateway,
            IAlmacenamientoGateway almacenamientoGateway,
            INotificacionAdminGateway notificacionAdminGateway) {
        return new VerificarRegistroVendedorInteractor(vendedorGateway, almacenamientoGateway, notificacionAdminGateway);
    }
}
