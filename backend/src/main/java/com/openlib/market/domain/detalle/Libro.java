package com.openlib.market.domain.detalle;

import com.openlib.market.domain.soporte.TransicionEstadoInvalidaException;

public class Libro extends ContenidoDigital {

    public Libro(Isbn isbn, String titulo, String sinopsis, Precio precio, String urlPortada) {
        this(isbn, titulo, sinopsis, precio, urlPortada, null, null);
    }

    public Libro(Isbn isbn, String titulo, String sinopsis, Precio precio, String urlPortada, String categoria) {
        this(isbn, titulo, sinopsis, precio, urlPortada, categoria, null);
    }

    public Libro(Isbn isbn, String titulo, String sinopsis, Precio precio, String urlPortada, String categoria, String idVendedor) {
        this(isbn, titulo, sinopsis, precio, urlPortada, categoria, idVendedor, EstadoLibro.PENDIENTE, null);
    }

    public Libro(Isbn isbn, String titulo, String sinopsis, Precio precio, String urlPortada, String categoria, String idVendedor, EstadoLibro estado, String urlVistaPrevia) {
        super(isbn, titulo, sinopsis, precio, urlPortada, categoria, idVendedor, TipoFormato.LIBRO, estado, urlVistaPrevia);
        if (isbn == null || titulo == null || titulo.trim().isEmpty() || sinopsis == null || sinopsis.trim().isEmpty() || precio == null) {
            throw new IllegalArgumentException("Datos obligatorios del libro incompletos");
        }
    }

    public Isbn getIsbn() { return getId(); }

    @Override
    public Libro actualizarPrecio(Precio nuevoPrecio) {
        if (nuevoPrecio == null) {
            throw new IllegalArgumentException("El nuevo precio no puede ser nulo");
        }
        return new Libro(getId(), getTitulo(), getSinopsis(), nuevoPrecio, getUrlPortada(), getCategoria(), getIdVendedor(), getEstado(), getUrlVistaPrevia());
    }

    @Override
    public Libro pausar() {
        if (getEstado() == EstadoLibro.PAUSADO) {
            throw new IllegalStateException("El libro ya se encuentra pausado");
        }
        if (getEstado() == EstadoLibro.BLOQUEADO) {
            throw new IllegalStateException("No se puede pausar un libro que ha sido bloqueado por administración");
        }
        return new Libro(getId(), getTitulo(), getSinopsis(), getPrecio(), getUrlPortada(), getCategoria(), getIdVendedor(), EstadoLibro.PAUSADO, getUrlVistaPrevia());
    }

    @Override
    public Libro reanudar() {
        if (getEstado() == EstadoLibro.RECHAZADO || getEstado() == EstadoLibro.BLOQUEADO) {
            throw new com.openlib.market.domain.detalle.TransicionEstadoInvalidaException("No se puede alterar el estado de un libro bloqueado o rechazado");
        }
        if (getEstado() == EstadoLibro.PUBLICADO) {
            throw new com.openlib.market.domain.shared.AccionNoPermitidaException("El libro ya está publicado");
        }
        if (getEstado() != EstadoLibro.PAUSADO) {
            throw new com.openlib.market.domain.detalle.TransicionEstadoInvalidaException("Solo los libros PAUSADOS pueden ser reanudados");
        }
        return new Libro(getId(), getTitulo(), getSinopsis(), getPrecio(), getUrlPortada(), getCategoria(), getIdVendedor(), EstadoLibro.PUBLICADO, getUrlVistaPrevia());
    }

    @Override
    public Libro aprobar() {
        if (getEstado() != EstadoLibro.PENDIENTE) {
            throw new TransicionEstadoInvalidaException("Solo los libros en revisión (PENDIENTE) pueden ser aprobados");
        }
        return new Libro(getId(), getTitulo(), getSinopsis(), getPrecio(), getUrlPortada(), getCategoria(), getIdVendedor(), EstadoLibro.PUBLICADO, getUrlVistaPrevia());
    }

    @Override
    public Libro rechazar(com.openlib.market.domain.curaduria.MotivoRechazo motivo) {
        if (motivo == null) {
            throw new IllegalArgumentException("El motivo de rechazo no puede ser nulo");
        }
        if (getEstado() == EstadoLibro.RECHAZADO) {
            throw new com.openlib.market.domain.shared.AccionNoPermitidaException("El libro ya se encuentra rechazado");
        }
        return new Libro(getId(), getTitulo(), getSinopsis(), getPrecio(), getUrlPortada(), getCategoria(), getIdVendedor(), EstadoLibro.RECHAZADO, getUrlVistaPrevia());
    }

    @Override
    public boolean requiereControlDeInventario() {
        return true;
    }

}
