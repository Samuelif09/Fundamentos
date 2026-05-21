package com.openlib.market.domain.api;

import java.security.SecureRandom;
import java.util.Base64;

public record ApiKey(String valor) {
    public ApiKey {
        if (valor == null || valor.trim().isEmpty() || valor.length() < 32) {
            throw new IllegalArgumentException("La API Key debe tener al menos 32 caracteres");
        }
    }

    public static ApiKey generarNueva() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return new ApiKey(Base64.getUrlEncoder().withoutPadding().encodeToString(bytes));
    }
}
