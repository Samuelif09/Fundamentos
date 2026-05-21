package com.openlib.market.application.autenticacion;

public class LoginResponseDto {
    private final String token;
    private final boolean requiereMfa;

    public LoginResponseDto(String token) {
        this(token, false);
    }

    public LoginResponseDto(String token, boolean requiereMfa) {
        this.token = token;
        this.requiereMfa = requiereMfa;
    }

    public String getToken() { return token; }
    public boolean isRequiereMfa() { return requiereMfa; }
}
