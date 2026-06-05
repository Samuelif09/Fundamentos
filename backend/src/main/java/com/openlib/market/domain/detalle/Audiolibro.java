package com.openlib.market.domain.detalle;

import com.openlib.market.domain.soporte.TransicionEstadoInvalidaException;

public class Audiolibro extends ContenidoDigital {
    
    private final DuracionEnMinutos duracion;

    public Audiolibro(Isbn isbn, String titulo, String sinopsis, Precio precio, String urlPortada, String categoria, String idVendedor, DuracionEnMinutos duracion) {
        this(isbn, titulo, sinopsis, precio, urlPortada, categoria, idVendedor, EstadoLibro.PENDIENTE, null, duracion);
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
        if (getEstado() == EstadoLibro.RECHAZADO || getEstado() == EstadoLibro.BLOQUEADO) {
            throw new TransicionEstadoInvalidaException("No se puede alterar el estado de un audiolibro bloqueado o rechazado");
        }
        if (getEstado() == EstadoLibro.PAUSADO) {
            throw new com.openlib.market.domain.shared.AccionNoPermitidaException("El audiolibro ya se encuentra pausado");
        }
        if (getEstado() != EstadoLibro.PUBLICADO) {
            throw new TransicionEstadoInvalidaException("Solo los audiolibros PUBLICADOS pueden ser pausados");
        }
        return new Audiolibro(getId(), getTitulo(), getSinopsis(), getPrecio(), getUrlPortada(), getCategoria(), getIdVendedor(), EstadoLibro.PAUSADO, getUrlVistaPrevia(), getDuracion());
    }

    @Override
    public Audiolibro aprobar() {
        if (getEstado() != EstadoLibro.PENDIENTE) {
            throw new TransicionEstadoInvalidaException("Solo los audiolibros en revisión (PENDIENTE) pueden ser aprobados");
        }
        return new Audiolibro(getId(), getTitulo(), getSinopsis(), getPrecio(), getUrlPortada(), getCategoria(), getIdVendedor(), EstadoLibro.PUBLICADO, getUrlVistaPrevia(), getDuracion());
    }

    @Override
    public Audiolibro reanudar() {
        if (getEstado() == EstadoLibro.RECHAZADO || getEstado() == EstadoLibro.BLOQUEADO) {
            throw new com.openlib.market.domain.detalle.TransicionEstadoInvalidaException("No se puede alterar el estado de un audiolibro bloqueado o rechazado");
        }
        if (getEstado() == EstadoLibro.PUBLICADO) {
            throw new com.openlib.market.domain.shared.AccionNoPermitidaException("El audiolibro ya está publicado");
        }
        if (getEstado() != EstadoLibro.PAUSADO) {
            throw new com.openlib.market.domain.detalle.TransicionEstadoInvalidaException("Solo los audiolibros PAUSADOS pueden ser reanudados");
        }
        return new Audiolibro(getId(), getTitulo(), getSinopsis(), getPrecio(), getUrlPortada(), getCategoria(), getIdVendedor(), EstadoLibro.PUBLICADO, getUrlVistaPrevia(), getDuracion());
    }



    @Override
    public Audiolibro rechazar(com.openlib.market.domain.curaduria.MotivoRechazo motivo) {
        if (motivo == null) {
            throw new IllegalArgumentException("El motivo de rechazo no puede ser nulo");
        }
        if (getEstado() == EstadoLibro.RECHAZADO) {
            throw new com.openlib.market.domain.shared.AccionNoPermitidaException("El audiolibro ya se encuentra rechazado");
        }
        if (getEstado() != EstadoLibro.PENDIENTE) {
            throw new TransicionEstadoInvalidaException("Solo los audiolibros en revisión (PENDIENTE) pueden ser rechazados");
        }
        return new Audiolibro(getId(), getTitulo(), getSinopsis(), getPrecio(), getUrlPortada(), getCategoria(), getIdVendedor(), EstadoLibro.RECHAZADO, getUrlVistaPrevia(), getDuracion());
    }

    @Override
    public boolean requiereControlDeInventario() {
        return false;
    }
}
