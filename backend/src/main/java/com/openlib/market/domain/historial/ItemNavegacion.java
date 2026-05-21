package com.openlib.market.domain.historial;

import java.time.LocalDateTime;

public class ItemNavegacion {
    private final String idLibro;
    private LocalDateTime fechaVista;

    public ItemNavegacion(String idLibro, LocalDateTime fechaVista) {
        if (idLibro == null || idLibro.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del libro es obligatorio");
        }
        if (fechaVista == null) {
            throw new IllegalArgumentException("La fecha vista es obligatoria");
        }
        this.idLibro = idLibro;
        this.fechaVista = fechaVista;
    }

    public String getIdLibro() { return idLibro; }
    public LocalDateTime getFechaVista() { return fechaVista; }

    public void actualizarFechaVista(LocalDateTime nuevaFecha) {
        this.fechaVista = nuevaFecha;
    }
}
