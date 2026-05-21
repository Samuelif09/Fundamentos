package com.openlib.market.application.transaccionesAdmin;

public class TransaccionGlobalDto {
    private final String idPedido;
    private final String idComprador;
    private final double montoTotal;
    private final String estado;
    private final String fecha;
    private final String metodoPago;

    public TransaccionGlobalDto(String idPedido, String idComprador, double montoTotal, String estado, String fecha, String metodoPago) {
        this.idPedido = idPedido;
        this.idComprador = idComprador;
        this.montoTotal = montoTotal;
        this.estado = estado;
        this.fecha = fecha;
        this.metodoPago = metodoPago;
    }

    public String getIdPedido() { return idPedido; }
    public String getIdComprador() { return idComprador; }
    public double getMontoTotal() { return montoTotal; }
    public String getEstado() { return estado; }
    public String getFecha() { return fecha; }
    public String getMetodoPago() { return metodoPago; }
}
