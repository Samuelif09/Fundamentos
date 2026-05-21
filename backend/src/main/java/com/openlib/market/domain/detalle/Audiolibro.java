package com.openlib.market.domain.detalle;

public class Audiolibro extends ContenidoDigital {
    
    private final DuracionEnMinutos duracion;

    public Audiolibro(Isbn isbn, String titulo, String sinopsis, Precio precio, String urlPortada, String categoria, String idVendedor, DuracionEnMinutos duracion) {
        this(isbn, titulo, sinopsis, precio, urlPortada, categoria, idVendedor, EstadoLibro.ACTIVO, null, duracion);
    }

    public Audiolibro(Isbn isbn, String titulo, String sinopsis, Precio precio, String urlPortada, String categoria, String idVendedor, EstadoLibro estado, String urlVistaPrevia, DuracionEnMinutos duracion) {
        super(isbn, titulo, sinopsis, precio, urlPortada, categoria, idVendedor, TipoFormato.AUDIOLIBRO, estado, urlVistaPrevia);
        if (duracion == null) {
            throw new IllegalArgumentException("La duración es obligatoria para un audiolibro");
        }
        this.duracion = duracion;
    }

    public DuracionEnMinutos getDuracion() {
        return duracion;
    }

    @Override
    public Audiolibro actualizarPrecio(Precio nuevoPrecio) {
        if (nuevoPrecio == null) {
            throw new IllegalArgumentException("El nuevo precio no puede ser nulo");
        }
        return new Audiolibro(getId(), getTitulo(), getSinopsis(), nuevoPrecio, getUrlPortada(), getCategoria(), getIdVendedor(), getEstado(), getUrlVistaPrevia(), this.duracion);
    }

    @Override
    public Audiolibro pausar() {
        if (getEstado() == EstadoLibro.PAUSADO) {
            throw new IllegalStateException("El audiolibro ya se encuentra pausado");
        }
        if (getEstado() == EstadoLibro.BLOQUEADO) {
            throw new IllegalStateException("No se puede pausar un audiolibro que ha sido bloqueado por administración");
        }
        return new Audiolibro(getId(), getTitulo(), getSinopsis(), getPrecio(), getUrlPortada(), getCategoria(), getIdVendedor(), EstadoLibro.PAUSADO, getUrlVistaPrevia(), this.duracion);
    }

    @Override
    public Audiolibro rechazar(com.openlib.market.domain.curaduria.MotivoRechazo motivo) {
        if (motivo == null) {
            throw new IllegalArgumentException("El motivo de rechazo no puede ser nulo");
        }
        if (getEstado() == EstadoLibro.RECHAZADO) {
            throw new IllegalStateException("El audiolibro ya se encuentra rechazado");
        }
        return new Audiolibro(getId(), getTitulo(), getSinopsis(), getPrecio(), getUrlPortada(), getCategoria(), getIdVendedor(), EstadoLibro.RECHAZADO, getUrlVistaPrevia(), this.duracion);
    }
}
