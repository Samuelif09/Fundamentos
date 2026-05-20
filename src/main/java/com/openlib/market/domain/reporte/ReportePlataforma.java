package com.openlib.market.domain.reporte;

public class ReportePlataforma {
    private final TipoReporte tipo;
    private final String titulo;
    private final String[] cabeceras;
    private final java.util.List<String[]> filas;

    public ReportePlataforma(TipoReporte tipo, String titulo, String[] cabeceras, java.util.List<String[]> filas) {
        this.tipo = tipo;
        this.titulo = titulo;
        this.cabeceras = cabeceras;
        this.filas = filas;
    }

    public TipoReporte getTipo() { return tipo; }
    public String getTitulo() { return titulo; }
    public String[] getCabeceras() { return cabeceras; }
    public java.util.List<String[]> getFilas() { return filas; }
    
    public boolean isVacio() {
        return filas == null || filas.isEmpty();
    }
}
