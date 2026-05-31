package com.openlib.market.application.autenticacion;

public class LoginResponseDto {
    private final String token;
    private final boolean requiereMfa;
    private final String rol;

    public LoginResponseDto(String token, String rol) {
        this(token, false, rol);
    }

    public LoginResponseDto(String token, boolean requiereMfa, String rol) {
        this.token = token;
        this.requiereMfa = requiereMfa;
        this.rol = rol;
    }

    public String getToken() { return token; }
    public boolean isRequiereMfa() { return requiereMfa; }
    public String getRol() { return rol; }
}
