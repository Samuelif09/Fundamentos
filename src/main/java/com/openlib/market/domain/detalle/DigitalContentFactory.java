package com.openlib.market.domain.detalle;

public class DigitalContentFactory {
    public static ContenidoDigital crear(TipoFormato tipo, Isbn isbn, String titulo, String sinopsis, Precio precio, String urlPortada, String categoria, String idVendedor, Integer duracionMinutos) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de formato es requerido");
        }

        switch (tipo) {
            case LIBRO:
                return new Libro(isbn, titulo, sinopsis, precio, urlPortada, categoria, idVendedor);
            case AUDIOLIBRO:
                if (duracionMinutos == null) {
                    throw new IllegalArgumentException("La duración en minutos es requerida para un audiolibro");
                }
                return new Audiolibro(isbn, titulo, sinopsis, precio, urlPortada, categoria, idVendedor, new DuracionEnMinutos(duracionMinutos));
            case CURSO_VIRTUAL:
                if (duracionMinutos == null) {
                    throw new IllegalArgumentException("La duración estimada en minutos es requerida para un curso virtual");
                }
                return new CursoVirtual(isbn, titulo, sinopsis, precio, urlPortada, categoria, idVendedor, new DuracionEnMinutos(duracionMinutos));
            default:
                throw new IllegalArgumentException("Tipo de formato no soportado: " + tipo);
        }
    }
}
