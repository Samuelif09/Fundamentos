package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.autenticacion.ITokenGeneratorGateway;
import com.openlib.market.domain.autenticacion.TokenAcceso;
import com.openlib.market.domain.autenticacion.UsuarioAuth;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Primary
public class JwtTokenGateway implements ITokenGeneratorGateway {

    @Override
    public TokenAcceso generar(UsuarioAuth usuario) {
        // En una implementación real, aquí se usaría la librería JWT (ej. io.jsonwebtoken:jjwt)
        // para firmar con HMAC y generar el string base64url.
        // Como este es el MVP de persistencia y no requiere dependencias de red/crypto adicionales
        // para la prueba, simularemos la firma.
        
        String tokenSimulado = "jwt-hmac-sha256." + usuario.getId() + "." + System.currentTimeMillis();
        return new TokenAcceso(tokenSimulado);
    }
}
