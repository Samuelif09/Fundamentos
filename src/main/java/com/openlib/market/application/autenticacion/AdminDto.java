package com.openlib.market.application.autenticacion;

import java.util.List;

public class AdminDto {
    private final String id;
    private final String nombre;
    private final String email;
    private final List<String> roles;

    public AdminDto(String id, String nombre, String email, List<String> roles) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.roles = roles;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public List<String> getRoles() { return roles; }
}
