package com.openlib.market.domain.detalle;

public class CursoVirtual extends ContenidoDigital {
    
    private final DuracionEnMinutos duracionEstimada;

    public CursoVirtual(Isbn isbn, String titulo, String sinopsis, Precio precio, String urlPortada, String categoria, String idVendedor, DuracionEnMinutos duracionEstimada) {
        this(isbn, titulo, sinopsis, precio, urlPortada, categoria, idVendedor, EstadoLibro.PENDIENTE, null, duracionEstimada);
    }

    public CursoVirtual(Isbn isbn, String titulo, String sinopsis, Precio precio, String urlPortada, String categoria, String idVendedor, EstadoLibro estado, String urlVistaPrevia, DuracionEnMinutos duracionEstimada) {
        super(isbn, titulo, sinopsis, precio, urlPortada, categoria, idVendedor, TipoFormato.CURSO_VIRTUAL, estado, urlVistaPrevia);
        if (duracionEstimada == null) {
            throw new IllegalArgumentException("La duración estimada es obligatoria para un curso");
        }
        this.duracionEstimada = duracionEstimada;
    }

    public DuracionEnMinutos getDuracionEstimada() {
        return duracionEstimada;
    }

    @Override
    public CursoVirtual actualizarPrecio(Precio nuevoPrecio) {
        if (nuevoPrecio == null) {
            throw new IllegalArgumentException("El nuevo precio no puede ser nulo");
        }
        return new CursoVirtual(getId(), getTitulo(), getSinopsis(), nuevoPrecio, getUrlPortada(), getCategoria(), getIdVendedor(), getEstado(), getUrlVistaPrevia(), this.duracionEstimada);
    }

    @Override
    public CursoVirtual pausar() {
        if (getEstado() == EstadoLibro.PAUSADO) {
            throw new IllegalStateException("El curso ya se encuentra pausado");
        }
        if (getEstado() == EstadoLibro.BLOQUEADO) {
            throw new IllegalStateException("No se puede pausar un curso que ha sido bloqueado por administración");
        }
        return new CursoVirtual(getId(), getTitulo(), getSinopsis(), getPrecio(), getUrlPortada(), getCategoria(), getIdVendedor(), EstadoLibro.PAUSADO, getUrlVistaPrevia(), this.duracionEstimada);
    }

    @Override
    public CursoVirtual reanudar() {
        if (getEstado() == EstadoLibro.RECHAZADO || getEstado() == EstadoLibro.BLOQUEADO) {
            throw new com.openlib.market.domain.detalle.TransicionEstadoInvalidaException("No se puede alterar el estado de un curso bloqueado o rechazado");
        }
        if (getEstado() == EstadoLibro.PUBLICADO) {
            throw new com.openlib.market.domain.shared.AccionNoPermitidaException("El curso ya está publicado");
        }
        if (getEstado() != EstadoLibro.PAUSADO) {
            throw new com.openlib.market.domain.detalle.TransicionEstadoInvalidaException("Solo los cursos PAUSADOS pueden ser reanudados");
        }
        return new CursoVirtual(getId(), getTitulo(), getSinopsis(), getPrecio(), getUrlPortada(), getCategoria(), getIdVendedor(), EstadoLibro.PUBLICADO, getUrlVistaPrevia(), getDuracionEstimada());
    }

    @Override
    public CursoVirtual aprobar() {
        if (getEstado() != EstadoLibro.PENDIENTE) {
            throw new com.openlib.market.domain.detalle.TransicionEstadoInvalidaException("Solo los cursos en revisión (PENDIENTE) pueden ser aprobados");
        }
        return new CursoVirtual(getId(), getTitulo(), getSinopsis(), getPrecio(), getUrlPortada(), getCategoria(), getIdVendedor(), EstadoLibro.PUBLICADO, getUrlVistaPrevia(), getDuracionEstimada());
    }

    @Override
    public CursoVirtual rechazar(com.openlib.market.domain.curaduria.MotivoRechazo motivo) {
        if (motivo == null) {
            throw new IllegalArgumentException("El motivo de rechazo no puede ser nulo");
        }
        if (getEstado() == EstadoLibro.RECHAZADO) {
            throw new IllegalStateException("El curso ya se encuentra rechazado");
        }
        return new CursoVirtual(getId(), getTitulo(), getSinopsis(), getPrecio(), getUrlPortada(), getCategoria(), getIdVendedor(), EstadoLibro.RECHAZADO, getUrlVistaPrevia(), this.duracionEstimada);
    }
}
