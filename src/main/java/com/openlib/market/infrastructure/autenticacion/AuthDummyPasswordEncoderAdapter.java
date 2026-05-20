package com.openlib.market.infrastructure.autenticacion;

import com.openlib.market.domain.autenticacion.IVerificadorPasswordGateway;
import com.openlib.market.domain.autenticacion.PasswordPlano;
import org.springframework.stereotype.Component;

@Component
public class AuthDummyPasswordEncoderAdapter implements IVerificadorPasswordGateway {

    @Override
    public boolean verificar(PasswordPlano passwordPlano, String hashAlmacenado) {
        // En la Entrega 1, la verificación es un match exacto si no hay bcrypt integrado
        // En Entrega 2, esto usaría BCrypt.checkpw(passwordPlano.getValor(), hashAlmacenado)
        return passwordPlano.getValor().equals(hashAlmacenado);
    }
}
