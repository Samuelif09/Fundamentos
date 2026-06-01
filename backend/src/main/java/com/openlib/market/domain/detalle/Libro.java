package com.openlib.market.domain.detalle;

public class Libro extends ContenidoDigital {

    private final int stock;

    public Libro(Isbn isbn, String titulo, String sinopsis, Precio precio, String urlPortada) {
        this(isbn, titulo, sinopsis, precio, urlPortada, null, null);
    }

    public Libro(Isbn isbn, String titulo, String sinopsis, Precio precio, String urlPortada, String categoria) {
        this(isbn, titulo, sinopsis, precio, urlPortada, categoria, null);
    }

    public Libro(Isbn isbn, String titulo, String sinopsis, Precio precio, String urlPortada, String categoria, String idVendedor) {
        this(isbn, titulo, sinopsis, precio, urlPortada, categoria, idVendedor, EstadoLibro.ACTIVO, null);
    }

    public Libro(Isbn isbn, String titulo, String sinopsis, Precio precio, String urlPortada, String categoria, String idVendedor, int stock) {
        this(isbn, titulo, sinopsis, precio, urlPortada, categoria, idVendedor, EstadoLibro.ACTIVO, null, stock);
    }

    public Libro(Isbn isbn, String titulo, String sinopsis, Precio precio, String urlPortada, String categoria, String idVendedor, EstadoLibro estado, String urlVistaPrevia) {
        this(isbn, titulo, sinopsis, precio, urlPortada, categoria, idVendedor, estado, urlVistaPrevia, 10);
    }

    public Libro(Isbn isbn, String titulo, String sinopsis, Precio precio, String urlPortada, String categoria, String idVendedor, EstadoLibro estado, String urlVistaPrevia, int stock) {
        super(isbn, titulo, sinopsis, precio, urlPortada, categoria, idVendedor, TipoFormato.LIBRO, estado, urlVistaPrevia);
        if (isbn == null || titulo == null || titulo.trim().isEmpty() || sinopsis == null || sinopsis.trim().isEmpty() || precio == null) {
            throw new IllegalArgumentException("Datos obligatorios del libro incompletos");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser un número negativo.");
        }
        this.stock = stock;
    }

    public int getStock() { return stock; }

    public Isbn getIsbn() { return getId(); }

    @Override
    public Libro actualizarPrecio(Precio nuevoPrecio) {
        if (nuevoPrecio == null) {
            throw new IllegalArgumentException("El nuevo precio no puede ser nulo");
        }
        return new Libro(getId(), getTitulo(), getSinopsis(), nuevoPrecio, getUrlPortada(), getCategoria(), getIdVendedor(), getEstado(), getUrlVistaPrevia(), stock);
    }

    @Override
    public Libro pausar() {
        if (getEstado() == EstadoLibro.PAUSADO) {
            throw new IllegalStateException("El libro ya se encuentra pausado");
        }
        if (getEstado() == EstadoLibro.BLOQUEADO) {
            throw new IllegalStateException("No se puede pausar un libro que ha sido bloqueado por administración");
        }
        return new Libro(getId(), getTitulo(), getSinopsis(), getPrecio(), getUrlPortada(), getCategoria(), getIdVendedor(), EstadoLibro.PAUSADO, getUrlVistaPrevia(), stock);
    }

    @Override
    public Libro rechazar(com.openlib.market.domain.curaduria.MotivoRechazo motivo) {
        if (motivo == null) {
            throw new IllegalArgumentException("El motivo de rechazo no puede ser nulo");
        }
        if (getEstado() == EstadoLibro.RECHAZADO) {
            throw new IllegalStateException("El libro ya se encuentra rechazado");
        }
        return new Libro(getId(), getTitulo(), getSinopsis(), getPrecio(), getUrlPortada(), getCategoria(), getIdVendedor(), EstadoLibro.RECHAZADO, getUrlVistaPrevia(), stock);
    }
}
