package com.openlib.market.domain.finanzas;

public class DatosFiscalesVendedor {
    private final String idVendedor;
    private final String identificacionTributaria;
    private final String razonSocial;

    public DatosFiscalesVendedor(String idVendedor, String identificacionTributaria, String razonSocial) {
        if (idVendedor == null || idVendedor.isBlank()) throw new IllegalArgumentException("ID del vendedor requerido");
        if (identificacionTributaria == null || identificacionTributaria.isBlank()) throw new IllegalArgumentException("Identificación tributaria requerida");
        if (razonSocial == null || razonSocial.isBlank()) throw new IllegalArgumentException("Razón social requerida");

        this.idVendedor = idVendedor;
        this.identificacionTributaria = identificacionTributaria;
        this.razonSocial = razonSocial;
    }

    public String getIdVendedor() { return idVendedor; }
    public String getIdentificacionTributaria() { return identificacionTributaria; }
    public String getRazonSocial() { return razonSocial; }
}
