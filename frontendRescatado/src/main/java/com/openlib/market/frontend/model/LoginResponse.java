package com.openlib.market.frontend.model;

/**
 * Espejo del LoginResponseDto del backend.
 * JSON: {"token":"...", "requiereMfa": false, "userId":"..."}
 */
public class LoginResponse {
    private String token;
    private boolean requiereMfa;
    private String rol;
    private String usuarioId;

    public LoginResponse() {}

    public String getToken()         { return token; }
    public boolean isRequiereMfa()   { return requiereMfa; }
    public String getRol()           { return rol; }
    public String getUsuarioId()     { return usuarioId; }
    public String getUserId()        { return usuarioId; } // Alias para retrocompatibilidad local
    public void setToken(String t)   { this.token = t; }
    public void setRequiereMfa(boolean r) { this.requiereMfa = r; }
    public void setRol(String rol)   { this.rol = rol; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public void setUserId(String userId) { this.usuarioId = userId; }
}
