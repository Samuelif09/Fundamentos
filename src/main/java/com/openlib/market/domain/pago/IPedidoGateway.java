package com.openlib.market.domain.pago;

public interface IPedidoGateway {
    void guardar(Pedido pedido);
    java.util.List<Pedido> listarPorUsuarioId(String idUsuario, int offset, int limit);
    java.util.List<Pedido> listarTodos(int page, int size);
    java.util.Optional<Pedido> obtenerPorId(String id);
}
