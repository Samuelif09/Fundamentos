package com.openlib.market.domain.historial;

import java.time.LocalDateTime;

public class LibroVistoEvent {
    private final String idUsuario;
    private final String idLibro;
    private final LocalDateTime fechaVista;

    public LibroVistoEvent(String idUsuario, String idLibro) {
        this.idUsuario = idUsuario;
        this.idLibro = idLibro;
        this.fechaVista = LocalDateTime.now();
    }

    public String getIdUsuario() { return idUsuario; }
    public String getIdLibro() { return idLibro; }
    public LocalDateTime getFechaVista() { return fechaVista; }
}
