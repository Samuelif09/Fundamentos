package com.openlib.market.infrastructure.autenticacion;

import com.openlib.market.domain.autenticacion.Email;
import com.openlib.market.domain.autenticacion.IEmailGateway;
import com.openlib.market.domain.autenticacion.TokenRecuperacion;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class DummyEmailGateway implements IEmailGateway {
    
    private static final Logger logger = Logger.getLogger(DummyEmailGateway.class.getName());

    @Override
    public void enviarTokenRecuperacion(Email email, TokenRecuperacion token) {
        String mensaje = String.format("Enviando correo de recuperación a: %s\nToken: %s\nExpira: %s",
                email.getDireccion(), token.getValor(), token.getFechaExpiracion());
        
        logger.info(mensaje);
        System.out.println("================= SIMULACIÓN DE EMAIL =================");
        System.out.println(mensaje);
        System.out.println("=======================================================");
    }
}
