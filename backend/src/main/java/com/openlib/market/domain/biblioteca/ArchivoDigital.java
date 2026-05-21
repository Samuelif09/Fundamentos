package com.openlib.market.domain.biblioteca;

public class ArchivoDigital {
    private final String url;
    private final String mimeType;
    private final byte[] contenidoFisico; // Utilizado para la Entrega 1 (simulación local)

    public ArchivoDigital(String url, String mimeType, byte[] contenidoFisico) {
        this.url = url;
        this.mimeType = mimeType;
        this.contenidoFisico = contenidoFisico;
    }

    public String getUrl() { return url; }
    public String getMimeType() { return mimeType; }
    public byte[] getContenidoFisico() { return contenidoFisico; }
}
