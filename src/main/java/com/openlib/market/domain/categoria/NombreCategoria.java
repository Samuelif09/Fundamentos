package com.openlib.market.domain.categoria;

public class NombreCategoria {
    private final String valor;

    public NombreCategoria(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio.");
        }
        String normalizado = valor.trim().toLowerCase()
                .replaceAll("\\s+", " ");
        // Capitalizar primera letra
        this.valor = normalizado.substring(0, 1).toUpperCase() + normalizado.substring(1);
    }

    public String getValor() { return valor; }

    public String getNormalizado() {
        return valor.toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NombreCategoria that = (NombreCategoria) o;
        return getNormalizado().equals(that.getNormalizado());
    }

    @Override
    public int hashCode() {
        return getNormalizado().hashCode();
    }
}
