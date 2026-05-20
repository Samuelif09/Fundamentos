package com.openlib.market.application.marketing;

public class BannerDto {
    private final String id;
    private final String titulo;
    private final String urlImagen;
    private final String urlDestino;
    private final String fechaInicio;
    private final String fechaFin;
    private final String estado;
    private final boolean vigente;

    public BannerDto(String id, String titulo, String urlImagen, String urlDestino, String fechaInicio, String fechaFin, String estado, boolean vigente) {
        this.id = id;
        this.titulo = titulo;
        this.urlImagen = urlImagen;
        this.urlDestino = urlDestino;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.vigente = vigente;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getUrlImagen() { return urlImagen; }
    public String getUrlDestino() { return urlDestino; }
    public String getFechaInicio() { return fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public String getEstado() { return estado; }
    public boolean isVigente() { return vigente; }
}
