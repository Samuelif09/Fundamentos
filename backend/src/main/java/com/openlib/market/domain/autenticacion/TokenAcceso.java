package com.openlib.market.domain.autenticacion;

public class TokenAcceso {
    private final String token;

    public TokenAcceso(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("El token no puede ser vacío");
        }
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
