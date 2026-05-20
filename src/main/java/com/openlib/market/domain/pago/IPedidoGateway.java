package com.openlib.market.domain.pago;

public interface IPedidoGateway {
    void guardar(Pedido pedido);
    java.util.List<Pedido> listarPorUsuarioId(String idUsuario, int offset, int limit);
}
