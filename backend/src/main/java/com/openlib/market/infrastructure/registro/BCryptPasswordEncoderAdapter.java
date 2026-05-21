package com.openlib.market.infrastructure.registro;

import com.openlib.market.domain.registro.IPasswordEncoderGateway;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class BCryptPasswordEncoderAdapter implements IPasswordEncoderGateway {

    @Override
    public String encode(String rawPassword) {
        // Entrega 1: Dummy encoding. En Entrega 2 se usa BCrypt real de Spring Security
        return Base64.getEncoder().encodeToString(rawPassword.getBytes());
    }
}
