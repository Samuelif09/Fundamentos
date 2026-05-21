package com.openlib.market.application.resena;

import java.time.LocalDate;

public class ResenaDto {
    private final String id;
    private final int calificacion;
    private final String texto;
    private final LocalDate fecha;

    public ResenaDto(String id, int calificacion, String texto, LocalDate fecha) {
        this.id = id;
        this.calificacion = calificacion;
        this.texto = texto;
        this.fecha = fecha;
    }

    public String getId() { return id; }
    public int getCalificacion() { return calificacion; }
    public String getTexto() { return texto; }
    public LocalDate getFecha() { return fecha; }
}
