package com.openlib.market.domain.finanzas;

public class DesgloseFinanciero {
    private final double montoBruto;
    private final double comisionPlataforma;
    private final double impuestos;
    private final double gananciaNeta;

    public DesgloseFinanciero(double montoBruto, double comisionPlataforma, double impuestos, double gananciaNeta) {
        this.montoBruto = redondear(montoBruto);
        this.comisionPlataforma = redondear(comisionPlataforma);
        this.impuestos = redondear(impuestos);
        this.gananciaNeta = redondear(gananciaNeta);

        verificarConsistencia();
    }

    private void verificarConsistencia() {
        double calculoEsperado = redondear(montoBruto - comisionPlataforma - impuestos);
        if (Math.abs(calculoEsperado - gananciaNeta) > 0.01) {
            throw new IllegalStateException(
                    String.format("Inconsistencia contable: Bruto (%.2f) - Comision (%.2f) - Impuestos (%.2f) != Neta (%.2f)",
                            montoBruto, comisionPlataforma, impuestos, gananciaNeta)
            );
        }
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    public double getMontoBruto() { return montoBruto; }
    public double getComisionPlataforma() { return comisionPlataforma; }
    public double getImpuestos() { return impuestos; }
    public double getGananciaNeta() { return gananciaNeta; }
}
