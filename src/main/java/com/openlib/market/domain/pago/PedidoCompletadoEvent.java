package com.openlib.market.domain.pago;

import java.util.List;

public class PedidoCompletadoEvent {
    private final String idPedido;
    private final String idUsuario;
    private final double totalPagado;
    private final List<String> isbns;

    public PedidoCompletadoEvent(String idPedido, String idUsuario, double totalPagado, List<String> isbns) {
        this.idPedido = idPedido;
        this.idUsuario = idUsuario;
        this.totalPagado = totalPagado;
        this.isbns = isbns != null ? isbns : List.of();
    }

    public String getIdPedido() { return idPedido; }
    public String getIdUsuario() { return idUsuario; }
    public double getTotalPagado() { return totalPagado; }
    public List<String> getIsbns() { return isbns; }
}
