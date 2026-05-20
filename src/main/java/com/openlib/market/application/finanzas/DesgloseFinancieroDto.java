package com.openlib.market.application.finanzas;

import java.time.LocalDate;

public class DesgloseFinancieroDto {
    private final String idTransaccion;
    private final LocalDate fecha;
    private final double montoBruto;
    private final double comisionPlataforma;
    private final double impuestos;
    private final double gananciaNeta;

    public DesgloseFinancieroDto(String idTransaccion, LocalDate fecha, double montoBruto, double comisionPlataforma, double impuestos, double gananciaNeta) {
        this.idTransaccion = idTransaccion;
        this.fecha = fecha;
        this.montoBruto = montoBruto;
        this.comisionPlataforma = comisionPlataforma;
        this.impuestos = impuestos;
        this.gananciaNeta = gananciaNeta;
    }

    public String getIdTransaccion() { return idTransaccion; }
    public LocalDate getFecha() { return fecha; }
    public double getMontoBruto() { return montoBruto; }
    public double getComisionPlataforma() { return comisionPlataforma; }
    public double getImpuestos() { return impuestos; }
    public double getGananciaNeta() { return gananciaNeta; }
}
