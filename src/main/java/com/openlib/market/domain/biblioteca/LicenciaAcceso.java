package com.openlib.market.domain.biblioteca;

public class LicenciaAcceso {
    private final String idUsuario;
    private final String idLibro;

    public LicenciaAcceso(String idUsuario, String idLibro) {
        if (idUsuario == null || idUsuario.isEmpty()) throw new IllegalArgumentException("Usuario inválido");
        if (idLibro == null || idLibro.isEmpty()) throw new IllegalArgumentException("Libro inválido");
        
        this.idUsuario = idUsuario;
        this.idLibro = idLibro;
    }

    public String getIdUsuario() { return idUsuario; }
    public String getIdLibro() { return idLibro; }
}
