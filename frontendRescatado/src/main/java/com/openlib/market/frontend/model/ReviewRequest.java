package com.openlib.market.frontend.model;

public class ReviewRequest {
    private String texto;
    private int calificacion;

    public ReviewRequest() {}

    public ReviewRequest(String texto, int calificacion) {
        this.texto = texto;
        this.calificacion = calificacion;
    }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public int getCalificacion() { return calificacion; }
    public void setCalificacion(int calificacion) { this.calificacion = calificacion; }
}
