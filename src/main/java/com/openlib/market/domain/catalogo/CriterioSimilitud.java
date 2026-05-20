package com.openlib.market.domain.catalogo;

public class CriterioSimilitud {
    private final String isbnExcluido;
    private final String categoria;
    private final int limite;

    public CriterioSimilitud(String isbnExcluido, String categoria, int limite) {
        if (isbnExcluido == null || isbnExcluido.trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN del libro base es obligatorio");
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría es obligatoria para buscar relacionados");
        }
        if (limite <= 0) {
            throw new IllegalArgumentException("El límite debe ser mayor que cero");
        }
        this.isbnExcluido = isbnExcluido.trim();
        this.categoria = categoria.trim();
        this.limite = limite;
    }

    public String getIsbnExcluido() { return isbnExcluido; }
    public String getCategoria() { return categoria; }
    public int getLimite() { return limite; }
}
