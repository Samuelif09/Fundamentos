package com.openlib.market.domain.marketing;

import java.time.LocalDateTime;
import java.util.UUID;

public class BannerPromocional {
    private final String id;
    private final String titulo;
    private final String urlImagen;
    private final String urlDestino;
    private final PeriodoCampana periodo;
    private EstadoCampana estado;

    public BannerPromocional(String titulo, String urlImagen, String urlDestino, PeriodoCampana periodo) {
        if (titulo == null || titulo.trim().isEmpty()) throw new IllegalArgumentException("El título es obligatorio");
        if (urlImagen == null || urlImagen.trim().isEmpty()) throw new IllegalArgumentException("La URL de la imagen es obligatoria");
        
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo;
        this.urlImagen = urlImagen;
        this.urlDestino = urlDestino;
        this.periodo = periodo;
        this.estado = EstadoCampana.ACTIVA;
    }

    public BannerPromocional(String id, String titulo, String urlImagen, String urlDestino, PeriodoCampana periodo, EstadoCampana estado) {
        this.id = id;
        this.titulo = titulo;
        this.urlImagen = urlImagen;
        this.urlDestino = urlDestino;
        this.periodo = periodo;
        this.estado = estado;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getUrlImagen() { return urlImagen; }
    public String getUrlDestino() { return urlDestino; }
    public PeriodoCampana getPeriodo() { return periodo; }
    public EstadoCampana getEstado() { return estado; }

    public void cambiarEstado(EstadoCampana nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public boolean estaVigente(LocalDateTime fechaActual) {
        if (estado == EstadoCampana.INACTIVA) {
            return false;
        }
        return periodo.incluye(fechaActual);
    }
}
