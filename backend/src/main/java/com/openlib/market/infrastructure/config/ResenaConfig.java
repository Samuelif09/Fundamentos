package com.openlib.market.infrastructure.config;

import com.openlib.market.application.resena.ILeerResenasUseCase;
import com.openlib.market.application.resena.IVerResenasUseCase;
import com.openlib.market.application.resena.LeerResenasInteractor;
import com.openlib.market.application.resena.VerResenasInteractor;
import com.openlib.market.application.resena.IResponderReputacionUseCase;
import com.openlib.market.application.resena.ResponderReputacionInteractor;
import com.openlib.market.domain.detalle.ILibroPublicacionGateway;
import com.openlib.market.domain.resena.IResenaGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResenaConfig {

    @Bean
    public IVerResenasUseCase verResenasUseCase(IResenaGateway resenaGateway) {
        return new VerResenasInteractor(resenaGateway);
    }

    @Bean
    public ILeerResenasUseCase leerResenasUseCase(IResenaGateway resenaGateway) {
        return new LeerResenasInteractor(resenaGateway);
    }

    @Bean
    public IResponderReputacionUseCase responderReputacionUseCase(
            IResenaGateway resenaGateway,
            ILibroPublicacionGateway libroPublicacionGateway) {
        return new ResponderReputacionInteractor(resenaGateway, libroPublicacionGateway);
    }

    @Bean
    public com.openlib.market.application.resena.IAgregarResenaUseCase agregarResenaUseCase(
            IResenaGateway resenaGateway) {
        return new com.openlib.market.application.resena.AgregarResenaInteractor(resenaGateway);
    }
}
