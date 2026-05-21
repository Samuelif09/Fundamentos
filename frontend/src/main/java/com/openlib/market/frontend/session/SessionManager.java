package com.openlib.market.frontend.session;

/**
 * Singleton que guarda el estado de sesión del usuario autenticado.
 * Almacena el token JWT para adjuntarlo a todas las peticiones autenticadas.
 */
public class SessionManager {

    private static SessionManager instance;

    private String token;
    private String email;
    private String rol;
    private String currentBookId;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void iniciarSesion(String token, String email, String rol) {
        this.token = token;
        this.email = email;
        this.rol = rol;
    }

    public void cerrarSesion() {
        this.token = null;
        this.email = null;
        this.rol = null;
    }

    public boolean estaAutenticado() {
        return token != null && !token.isEmpty();
    }

    public String getToken() { return token; }
    public String getEmail() { return email; }
    public String getRol()   { return rol; }

    public String getCurrentBookId() { return currentBookId; }
    public void setCurrentBookId(String currentBookId) { this.currentBookId = currentBookId; }
}
