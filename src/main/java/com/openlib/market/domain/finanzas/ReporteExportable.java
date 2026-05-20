package com.openlib.market.domain.finanzas;

public class ReporteExportable {
    private final String nombreArchivo;
    private final byte[] contenido;
    private final FormatoExportacion formato;

    public ReporteExportable(String nombreArchivo, byte[] contenido, FormatoExportacion formato) {
        this.nombreArchivo = nombreArchivo;
        this.contenido = contenido;
        this.formato = formato;
    }

    public String getNombreArchivo() { return nombreArchivo; }
    public byte[] getContenido() { return contenido; }
    public FormatoExportacion getFormato() { return formato; }
}
