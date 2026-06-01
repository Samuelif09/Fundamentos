package com.openlib.market.application.autenticacion;

public class LoginResponseDto {
    private final String token;
    private final boolean requiereMfa;
    private final String rol;
    private final String userId;

    public LoginResponseDto(String token, String rol) {
        this(token, false, rol, null);
    }

    public LoginResponseDto(String token, String rol, String userId) {
        this(token, false, rol, userId);
    }

    public LoginResponseDto(String token, boolean requiereMfa, String rol) {
        this(token, requiereMfa, rol, null);
    }

    public LoginResponseDto(String token, boolean requiereMfa, String rol, String userId) {
        this.token = token;
        this.requiereMfa = requiereMfa;
        this.rol = rol;
        this.userId = userId;
    }

    public String getToken() { return token; }
    public boolean isRequiereMfa() { return requiereMfa; }
    public String getRol() { return rol; }
    public String getUserId() { return userId; }
}
