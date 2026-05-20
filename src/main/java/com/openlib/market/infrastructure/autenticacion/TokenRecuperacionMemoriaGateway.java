package com.openlib.market.infrastructure.autenticacion;

import com.openlib.market.domain.autenticacion.Email;
import com.openlib.market.domain.autenticacion.ITokenRecuperacionGateway;
import com.openlib.market.domain.autenticacion.TokenRecuperacion;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenRecuperacionMemoriaGateway implements ITokenRecuperacionGateway {

    // Guarda el token asociado al email del usuario
    private final Map<String, TokenRecuperacion> tokens = new ConcurrentHashMap<>();

    @Override
    public void guardar(Email email, TokenRecuperacion token) {
        tokens.put(email.getDireccion(), token);
    }
}
