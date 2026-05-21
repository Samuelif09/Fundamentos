package com.openlib.market.frontend.model;

/**
 * Espejo del LoginResponseDto del backend.
 * JSON: {"token":"...", "requiereMfa": false}
 */
public class LoginResponse {
    private String token;
    private boolean requiereMfa;

    public LoginResponse() {}

    public String getToken()         { return token; }
    public boolean isRequiereMfa()   { return requiereMfa; }
    public void setToken(String t)   { this.token = t; }
    public void setRequiereMfa(boolean r) { this.requiereMfa = r; }
}
