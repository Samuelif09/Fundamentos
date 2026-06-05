package com.openlib.market.application.resena;

public class AgregarResenaRequestDto {
    private String texto;
    private int calificacion;

    public AgregarResenaRequestDto() {}

    public AgregarResenaRequestDto(String texto, int calificacion) {
        this.texto = texto;
        this.calificacion = calificacion;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }
}
