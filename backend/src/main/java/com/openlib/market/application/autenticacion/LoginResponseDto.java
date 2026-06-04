package com.openlib.market.application.autenticacion;

public class LoginResponseDto {
    private final String token;
    private final boolean requiereMfa;
    private final String usuarioId;

    public LoginResponseDto(String token, String usuarioId) {
        this(token, false, usuarioId);
    }

    public LoginResponseDto(String token, boolean requiereMfa, String usuarioId) {
        this.token = token;
        this.requiereMfa = requiereMfa;
        this.usuarioId = usuarioId;
    }

    public String getToken() { return token; }
    public boolean isRequiereMfa() { return requiereMfa; }
    public String getUsuarioId() { return usuarioId; }
}
