package com.openlib.market.domain.ventavendedor;

import java.util.List;

public class NotificacionVendedor {
    private final String idVendedor;
    private final String idPedido;
    private final List<String> isbnsVendidos;

    public NotificacionVendedor(String idVendedor, String idPedido, List<String> isbnsVendidos) {
        if (idVendedor == null || idPedido == null || isbnsVendidos == null || isbnsVendidos.isEmpty()) {
            throw new IllegalArgumentException("Datos de notificación incompletos");
        }
        this.idVendedor = idVendedor;
        this.idPedido = idPedido;
        this.isbnsVendidos = isbnsVendidos;
    }

    public String getIdVendedor() { return idVendedor; }
    public String getIdPedido() { return idPedido; }
    public List<String> getIsbnsVendidos() { return isbnsVendidos; }
}
