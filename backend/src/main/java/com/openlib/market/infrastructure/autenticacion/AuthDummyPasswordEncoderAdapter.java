package com.openlib.market.infrastructure.autenticacion;

import com.openlib.market.domain.autenticacion.IVerificadorPasswordGateway;
import com.openlib.market.domain.autenticacion.PasswordPlano;
import org.springframework.stereotype.Component;
import java.util.Base64; // <-- Importante añadir este import

@Component
public class AuthDummyPasswordEncoderAdapter implements IVerificadorPasswordGateway {

    @Override
    public boolean verificar(PasswordPlano passwordPlano, String hashAlmacenado) {
        String passDecodificado = new String(Base64.getDecoder().decode(hashAlmacenado));

        return passwordPlano.getValor().equals(passDecodificado);
    }
}