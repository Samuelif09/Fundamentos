package com.openlib.market.domain.almacenamiento;

import java.util.Set;

public class ArchivoImagen {
    private static final long MAX_BYTES = 2 * 1024 * 1024; // 2MB
    private static final Set<String> MIME_PERMITIDOS = Set.of("image/jpeg", "image/png", "image/webp");

    private final byte[] contenido;
    private final String mimeType;
    private final String nombreOriginal;

    public ArchivoImagen(byte[] contenido, String mimeType, String nombreOriginal) {
        if (contenido == null || contenido.length == 0) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }
        if (contenido.length > MAX_BYTES) {
            throw new IllegalArgumentException("El archivo supera el límite de 2MB");
        }
        if (!MIME_PERMITIDOS.contains(mimeType)) {
            throw new ArchivoInvalidoException("Tipo de archivo no permitido: " + mimeType);
        }
        this.contenido = contenido;
        this.mimeType = mimeType;
        this.nombreOriginal = nombreOriginal;
    }

    public byte[] getContenido() { return contenido; }
    public String getMimeType() { return mimeType; }
    public String getNombreOriginal() { return nombreOriginal; }
}
