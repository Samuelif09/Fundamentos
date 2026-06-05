package com.openlib.market.domain.checkout;

import com.openlib.market.domain.carrito.CarritoCompras;
import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.pago.TipoMetodoPago;

public class PedidoFactory {
    public Pedido crearDesdeCarrito(CarritoCompras carrito, String sesionId, double totalCalculado, String idUsuario, TipoMetodoPago metodoPago) {
        Pedido pedido = new Pedido(sesionId, totalCalculado, metodoPago);
        if (idUsuario != null && !idUsuario.isBlank()) {
            pedido.setIdUsuario(idUsuario);
        } else if (carrito.getIdUsuario() != null) {
            pedido.setIdUsuario(carrito.getIdUsuario().getId());
        }
        return pedido;
    }
}
