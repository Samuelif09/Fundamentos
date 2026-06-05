package com.openlib.market.frontend.session;

/**
 * Singleton que guarda el estado de sesión del usuario autenticado.
 * Almacena el token JWT para adjuntarlo a todas las peticiones autenticadas.
 */
public class SessionManager {

    private static SessionManager instance;

    private String  token;
    private String  email;
    private String  rol;
    private boolean isAdmin;
    private String  currentBookId;
    private String  userId;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /** Login estándar para clientes y vendedores. */
    public void iniciarSesion(String token, String email, String rol) {
        this.token   = token;
        this.email   = email;
        this.rol     = rol;
        this.isAdmin = false;
    }

    /** Login estándar para clientes y vendedores, con ID de usuario. */
    public void iniciarSesion(String token, String email, String rol, String userId) {
        this.token   = token;
        this.email   = email;
        this.rol     = rol;
        this.isAdmin = false;
        this.userId  = userId;
    }

    /** Login con privilegios elevados para administradores. */
    public void iniciarSesionAdmin(String token, String email) {
        this.token   = token;
        this.email   = email;
        this.rol     = "A";
        this.isAdmin = true;
    }

    public void cerrarSesion() {
        this.token   = null;
        this.email   = null;
        this.rol     = null;
        this.isAdmin = false;
        this.userId  = null;
    }

    public boolean estaAutenticado() {
        return token != null && !token.isEmpty();
    }

    public String  getToken()  { return token; }
    public String  getEmail()  { return email; }
    public String  getRol()    { return rol; }
    public boolean isAdmin()   { return isAdmin; }
    public String  getUserId() { return userId; }

    public String getCurrentBookId() { return currentBookId; }
    public void setCurrentBookId(String currentBookId) { this.currentBookId = currentBookId; }
}
