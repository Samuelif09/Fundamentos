package com.openlib.market.domain.tienda;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class UrlAmigable {
    private final String slug;

    public UrlAmigable(String textoOriginal) {
        if (textoOriginal == null || textoOriginal.trim().isEmpty()) {
            throw new IllegalArgumentException("El texto para la URL amigable no puede estar vacío");
        }
        this.slug = generarSlug(textoOriginal);
    }

    private String generarSlug(String texto) {
        String normalized = Normalizer.normalize(texto, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String sinTildes = pattern.matcher(normalized).replaceAll("");
        
        return sinTildes.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "") // Remover caracteres especiales
                .replaceAll("\\s+", "-")       // Espacios por guiones
                .replaceAll("-+", "-")         // Múltiples guiones por uno solo
                .replaceAll("^-|-$", "");      // Eliminar guiones al inicio o fin
    }

    public String getValor() {
        return slug;
    }
}
