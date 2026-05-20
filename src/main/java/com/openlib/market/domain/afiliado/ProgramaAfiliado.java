package com.openlib.market.domain.afiliado;

public class ProgramaAfiliado {
    private final String idVendedor;
    private final PorcentajeComisionAfiliado comisionAfiliado;
    private final double COMISION_PLATAFORMA = 15.0; // MVP hardcode

    public ProgramaAfiliado(String idVendedor, PorcentajeComisionAfiliado comisionAfiliado) {
        if (idVendedor == null || idVendedor.isBlank()) {
            throw new IllegalArgumentException("El ID del vendedor es requerido");
        }
        if (comisionAfiliado == null) {
            throw new IllegalArgumentException("La comisión del afiliado es requerida");
        }

        if (comisionAfiliado.getValor() + COMISION_PLATAFORMA > 100.0) {
            throw new ComisionInvalidaException("La suma de la comisión de la plataforma y del afiliado no puede exceder el 100%");
        }

        this.idVendedor = idVendedor;
        this.comisionAfiliado = comisionAfiliado;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public PorcentajeComisionAfiliado getComisionAfiliado() {
        return comisionAfiliado;
    }
}
