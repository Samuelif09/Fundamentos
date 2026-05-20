package com.openlib.market.infrastructure.autenticacion;

import com.openlib.market.domain.autenticacion.ITokenGeneratorGateway;
import com.openlib.market.domain.autenticacion.TokenAcceso;
import com.openlib.market.domain.autenticacion.UsuarioAuth;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenGeneratorAdapter implements ITokenGeneratorGateway {

    @Override
    public TokenAcceso generar(UsuarioAuth usuario) {
        // En un escenario real, esto construiría un JWT firmado con io.jsonwebtoken
        // Para el MVP (Entrega 1), devolvemos un UUID aleatorio simulando un session token.
        String dummyJwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." + 
                          UUID.randomUUID().toString() + 
                          ".signatureDummy";
        return new TokenAcceso(dummyJwt);
    }
}
