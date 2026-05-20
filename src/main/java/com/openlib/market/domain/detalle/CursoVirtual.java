package com.openlib.market.domain.detalle;

public class CursoVirtual extends ContenidoDigital {
    
    private final DuracionEnMinutos duracionEstimada;

    public CursoVirtual(Isbn isbn, String titulo, String sinopsis, Precio precio, String urlPortada, String categoria, String idVendedor, DuracionEnMinutos duracionEstimada) {
        this(isbn, titulo, sinopsis, precio, urlPortada, categoria, idVendedor, EstadoLibro.ACTIVO, null, duracionEstimada);
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
}
