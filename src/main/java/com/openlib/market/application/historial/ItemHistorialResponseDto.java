package com.openlib.market.application.historial;

import java.time.LocalDateTime;

public class ItemHistorialResponseDto {
    private final String idLibro;
    private final String titulo;
    private final LocalDateTime fechaVista;

    public ItemHistorialResponseDto(String idLibro, String titulo, LocalDateTime fechaVista) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.fechaVista = fechaVista;
    }

    public String getIdLibro() { return idLibro; }
    public String getTitulo() { return titulo; }
    public LocalDateTime getFechaVista() { return fechaVista; }
}
