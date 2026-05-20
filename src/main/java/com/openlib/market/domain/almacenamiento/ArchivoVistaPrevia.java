package com.openlib.market.domain.almacenamiento;

import java.util.Arrays;
import java.util.List;

public class ArchivoVistaPrevia {
    private final byte[] contenido;
    private final String tipoMime;
    private static final int MAX_SIZE_BYTES = 5 * 1024 * 1024; // 5 MB
    private static final List<String> TIPOS_PERMITIDOS = Arrays.asList("application/pdf", "application/epub+zip");

    public ArchivoVistaPrevia(byte[] contenido, String tipoMime) {
        if (contenido == null || contenido.length == 0) {
            throw new IllegalArgumentException("El archivo de vista previa no puede estar vacío");
        }
        if (contenido.length > MAX_SIZE_BYTES) {
            throw new IllegalArgumentException("El archivo excede el tamaño máximo permitido de 5MB");
        }
        if (tipoMime == null || !TIPOS_PERMITIDOS.contains(tipoMime.toLowerCase())) {
            throw new IllegalArgumentException("Formato de vista previa no permitido. Solo se acepta PDF o EPUB.");
        }
        
        this.contenido = contenido;
        this.tipoMime = tipoMime;
    }

    public byte[] getContenido() { return contenido; }
    public String getTipoMime() { return tipoMime; }
    
    public String obtenerExtension() {
        if ("application/pdf".equalsIgnoreCase(tipoMime)) return ".pdf";
        if ("application/epub+zip".equalsIgnoreCase(tipoMime)) return ".epub";
        return "";
    }
}
