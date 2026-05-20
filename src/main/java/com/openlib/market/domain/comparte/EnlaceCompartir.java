package com.openlib.market.domain.comparte;

public class EnlaceCompartir {
    private static final String BASE_URL = "https://openlib.market/libros/";
    private static final String UTM_PARAMS = "?utm_source=share";
    
    private final String url;

    public EnlaceCompartir(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN es requerido para generar el enlace");
        }
        this.url = BASE_URL + isbn.trim() + UTM_PARAMS;
    }

    public String getUrl() {
        return url;
    }
}
