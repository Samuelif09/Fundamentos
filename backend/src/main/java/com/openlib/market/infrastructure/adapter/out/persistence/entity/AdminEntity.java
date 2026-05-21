package com.openlib.market.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "administradores")
public class AdminEntity {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String hashContrasena;

    @Column(nullable = false)
    private String rol; // Será ROLE_ADMIN siempre, según el dominio

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "admin_roles", joinColumns = @JoinColumn(name = "admin_id"))
    private List<AdminRolEmbeddable> rolesAdmin = new ArrayList<>();

    public AdminEntity() {}

    public AdminEntity(String id, String email, String hashContrasena, String rol, List<AdminRolEmbeddable> rolesAdmin) {
        this.id = id;
        this.email = email;
        this.hashContrasena = hashContrasena;
        this.rol = rol;
        this.rolesAdmin = rolesAdmin != null ? rolesAdmin : new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHashContrasena() { return hashContrasena; }
    public void setHashContrasena(String hashContrasena) { this.hashContrasena = hashContrasena; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public List<AdminRolEmbeddable> getRolesAdmin() { return rolesAdmin; }
    public void setRolesAdmin(List<AdminRolEmbeddable> rolesAdmin) { this.rolesAdmin = rolesAdmin; }
}
