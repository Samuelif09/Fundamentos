package com.openlib.market.domain.resena;

import java.time.LocalDate;

public class Resena {
    private final String id;
    private final String isbnLibro;
    private final Calificacion calificacion;
    private final String texto;
    private final LocalDate fecha;
    private ComentarioRespuesta respuestaVendedor;

    public Resena(String id, String isbnLibro, Calificacion calificacion, String texto, LocalDate fecha) {
        if (isbnLibro == null || isbnLibro.trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN del libro es requerido");
        }
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El texto de la reseña no puede estar vacío");
        }
        this.id = id;
        this.isbnLibro = isbnLibro;
        this.calificacion = calificacion;
        this.texto = texto;
        this.fecha = fecha != null ? fecha : LocalDate.now();
    }

    public String getId() { return id; }
    public String getIsbnLibro() { return isbnLibro; }
    public Calificacion getCalificacion() { return calificacion; }
    public String getTexto() { return texto; }
    public LocalDate getFecha() { return fecha; }
    public ComentarioRespuesta getRespuestaVendedor() { return respuestaVendedor; }

    public void responder(ComentarioRespuesta respuesta) {
        if (this.respuestaVendedor != null) {
            throw new RespuestaDuplicadaException();
        }
        this.respuestaVendedor = respuesta;
    }
}
