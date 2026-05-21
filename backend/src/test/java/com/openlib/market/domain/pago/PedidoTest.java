package com.openlib.market.domain.pago;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    @Test
    void debeCrearPedidoEnEstadoPendiente() {
        Pedido pedido = new Pedido("sesion-123", 100.0, TipoMetodoPago.TARJETA);
        assertEquals(EstadoPedido.PENDIENTE, pedido.getEstado());
        assertEquals("sesion-123", pedido.getSesionId());
        assertEquals(100.0, pedido.getTotal());
        assertEquals(TipoMetodoPago.TARJETA, pedido.getTipoMetodoPago());
    }

    @Test
    void debeCambiarEstadoAPagado() {
        Pedido pedido = new Pedido("sesion-123", 100.0, TipoMetodoPago.TARJETA);
        pedido.marcarComoPagado();
        assertEquals(EstadoPedido.PAGADO, pedido.getEstado());
    }

    @Test
    void debeCambiarEstadoAFallido() {
        Pedido pedido = new Pedido("sesion-123", 100.0, TipoMetodoPago.TARJETA);
        pedido.marcarComoFallido();
        assertEquals(EstadoPedido.FALLIDO, pedido.getEstado());
    }

    @Test
    void noDebePermitirPagarSiNoEstaPendiente() {
        Pedido pedido = new Pedido("sesion-123", 100.0, TipoMetodoPago.TARJETA);
        pedido.marcarComoFallido();
        assertThrows(IllegalStateException.class, pedido::marcarComoPagado);
    }
}
