package com.openlib.market.domain.notificacion;

public class ReciboCompra {
    private final String idPedido;
    private final double totalPagado;

    public ReciboCompra(String idPedido, double totalPagado) {
        if (idPedido == null || idPedido.isEmpty()) {
            throw new IllegalArgumentException("El ID de pedido es requerido");
        }
        if (totalPagado < 0) {
            throw new IllegalArgumentException("El total no puede ser negativo");
        }
        this.idPedido = idPedido;
        this.totalPagado = totalPagado;
    }

    public String getIdPedido() { return idPedido; }
    public double getTotalPagado() { return totalPagado; }
}
