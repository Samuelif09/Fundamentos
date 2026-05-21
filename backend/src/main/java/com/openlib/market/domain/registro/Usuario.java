package com.openlib.market.domain.registro;

import java.util.UUID;

public class Usuario {
    private final String id;
    private final String nombre;
    private final Email email;
    private final Password password;
    private final RolUsuario rol;
    private EstadoCuenta estadoCuenta;
    private MotivoSuspension motivoSuspension;

    public Usuario(String nombre, Email email, Password password) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (email == null || password == null) {
            throw new IllegalArgumentException("El email y la contraseña son obligatorios");
        }
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = RolUsuario.VISITANTE; // Por defecto
        this.estadoCuenta = EstadoCuenta.ACTIVO;
    }

    public Usuario(String id, String nombre, Email email, Password password, RolUsuario rol) {
        this(id, nombre, email, password, rol, EstadoCuenta.ACTIVO, null);
    }

    public Usuario(String id, String nombre, Email email, Password password, RolUsuario rol, EstadoCuenta estadoCuenta, MotivoSuspension motivoSuspension) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.estadoCuenta = estadoCuenta != null ? estadoCuenta : EstadoCuenta.ACTIVO;
        this.motivoSuspension = motivoSuspension;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public Email getEmail() { return email; }
    public Password getPassword() { return password; }
    public RolUsuario getRol() { return rol; }
    public EstadoCuenta getEstadoCuenta() { return estadoCuenta; }
    public MotivoSuspension getMotivoSuspension() { return motivoSuspension; }

    public void suspender(MotivoSuspension motivo) {
        if (this.estadoCuenta == EstadoCuenta.SUSPENDIDO || this.estadoCuenta == EstadoCuenta.BANEADO) {
            throw new EstadoInvalidoException("El usuario ya se encuentra suspendido o baneado.");
        }
        this.estadoCuenta = EstadoCuenta.SUSPENDIDO;
        this.motivoSuspension = motivo;
    }
}
