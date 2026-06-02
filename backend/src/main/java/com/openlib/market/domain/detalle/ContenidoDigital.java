package com.openlib.market.domain.detalle;

import java.util.Objects;

public abstract class ContenidoDigital {
    private final Isbn id; // Reutilizamos Isbn o creamos un Id unificado, por ahora Isbn sirve de ID único en este dominio
    private final String titulo;
    private final String sinopsis;
    private Precio precio;
    private final String urlPortada;
    private final String categoria;
    private final String idVendedor;
    private final TipoFormato tipoFormato;
    private final EstadoLibro estado;
    private final String urlVistaPrevia;

    public ContenidoDigital(Isbn id, String titulo, String sinopsis, Precio precio, String urlPortada, String categoria, String idVendedor, TipoFormato tipoFormato, EstadoLibro estado, String urlVistaPrevia) {
        if (titulo == null || titulo.isBlank()) throw new IllegalArgumentException("El título es requerido");
        
        this.id = id;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.precio = precio;
        this.urlPortada = urlPortada;
        this.categoria = categoria;
        this.idVendedor = idVendedor;
        this.tipoFormato = tipoFormato;
        this.estado = estado != null ? estado : EstadoLibro.PENDIENTE;
        this.urlVistaPrevia = urlVistaPrevia;
    }

    public Isbn getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getSinopsis() { return sinopsis; }
    public Precio getPrecio() { return precio; }
    public String getUrlPortada() { return urlPortada; }
    public String getCategoria() { return categoria; }
    public String getIdVendedor() { return idVendedor; }
    public TipoFormato getTipoFormato() { return tipoFormato; }
    public EstadoLibro getEstado() { return estado; }
    public String getUrlVistaPrevia() { return urlVistaPrevia; }

    public abstract ContenidoDigital actualizarPrecio(Precio nuevoPrecio);
    public abstract ContenidoDigital pausar();
    public abstract ContenidoDigital reanudar();
    public abstract ContenidoDigital aprobar();
    public abstract ContenidoDigital rechazar(com.openlib.market.domain.curaduria.MotivoRechazo motivo);
}
