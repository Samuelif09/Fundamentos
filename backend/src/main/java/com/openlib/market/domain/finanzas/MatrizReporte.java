package com.openlib.market.domain.finanzas;

import java.util.List;

public class MatrizReporte {
    private final List<String> cabeceras;
    private final List<List<String>> filas;

    public MatrizReporte(List<String> cabeceras, List<List<String>> filas) {
        if (cabeceras == null || cabeceras.isEmpty()) {
            throw new IllegalArgumentException("Las cabeceras no pueden ser nulas o vacías");
        }
        if (filas == null) {
            throw new IllegalArgumentException("Las filas no pueden ser nulas");
        }
        this.cabeceras = cabeceras;
        this.filas = filas;
    }

    public List<String> getCabeceras() { return cabeceras; }
    public List<List<String>> getFilas() { return filas; }
}
