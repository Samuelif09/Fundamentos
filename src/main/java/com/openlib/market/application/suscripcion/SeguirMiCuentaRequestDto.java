package com.openlib.market.application.suscripcion;

public class SeguirMiCuentaRequestDto {
    private final String idComprador;
    private final String idVendedor;

    public SeguirMiCuentaRequestDto(String idComprador, String idVendedor) {
        this.idComprador = idComprador;
        this.idVendedor = idVendedor;
    }

    public String getIdComprador() { return idComprador; }
    public String getIdVendedor() { return idVendedor; }
}
