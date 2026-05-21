package com.openlib.market.domain.afiliado;

public class EnlaceAfiliado {
    private final String idAfiliado;
    private final String idVendedor;
    private final CodigoRastreo codigoRastreo;

    public EnlaceAfiliado(String idAfiliado, String idVendedor, CodigoRastreo codigoRastreo) {
        this.idAfiliado = idAfiliado;
        this.idVendedor = idVendedor;
        this.codigoRastreo = codigoRastreo;
    }

    public String getIdAfiliado() { return idAfiliado; }
    public String getIdVendedor() { return idVendedor; }
    public CodigoRastreo getCodigoRastreo() { return codigoRastreo; }

    public String generarUrl() {
        return "https://openlib.market/vendedor/" + idVendedor + "?ref=" + codigoRastreo.getValor();
    }
}
