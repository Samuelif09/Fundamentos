package com.openlib.market.domain.finanzas;

public class DatosFiscalesComprador {
    private final String idUsuario;
    private final String nombre;
    private final String correo;

    public DatosFiscalesComprador(String idUsuario, String nombre, String correo) {
        if (idUsuario == null || idUsuario.isBlank()) throw new IllegalArgumentException("ID de usuario requerido");
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre de comprador requerido");
        if (correo == null || correo.isBlank()) throw new IllegalArgumentException("Correo de comprador requerido");

        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
    }

    public String getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
}
