package com.openlib.market.domain.catalogo;

public class CriterioBusqueda {
    private final String titulo;
    private final String autor;
    private final String categoria;
    private final com.openlib.market.domain.filtroprecio.RangoPrecio rangoPrecio;

    public CriterioBusqueda(String titulo, String autor, String categoria) {
        this(titulo, autor, categoria, null);
    }

    public CriterioBusqueda(String titulo, String autor, String categoria,
                             com.openlib.market.domain.filtroprecio.RangoPrecio rangoPrecio) {
        String t = (titulo == null) ? "" : titulo.trim();
        String a = (autor == null) ? "" : autor.trim();
        String c = (categoria == null) ? "" : categoria.trim();

        if (t.isEmpty() && a.isEmpty() && c.isEmpty() && rangoPrecio == null) {
            throw new IllegalArgumentException("Debe proveer al menos un criterio de búsqueda (título, autor, categoría o rango de precio)");
        }

        this.titulo = t;
        this.autor = a;
        this.categoria = c;
        this.rangoPrecio = rangoPrecio;
    }

    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getCategoria() { return categoria; }
    public com.openlib.market.domain.filtroprecio.RangoPrecio getRangoPrecio() { return rangoPrecio; }
    public boolean tieneTitulo() { return !titulo.isEmpty(); }
    public boolean tieneAutor() { return !autor.isEmpty(); }
    public boolean tieneCategoria() { return !categoria.isEmpty(); }
    public boolean tieneRangoPrecio() { return rangoPrecio != null; }
}
