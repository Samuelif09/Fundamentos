package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.pago.EstadoPedido;
import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.pago.TipoMetodoPago;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.PedidoEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.mapper.PedidoMapper;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({PedidoJpaGateway.class, PedidoMapper.class})
@Transactional
class PedidoJpaGatewayIT {

    @Autowired
    private PedidoJpaGateway gateway;

    @Autowired
    private PedidoRepository repository;

    @Test
    void debeGuardarYRecuperarPedidoPorId() {
        Pedido pedido = new Pedido("sesion-123", 150000, TipoMetodoPago.TARJETA);
        pedido.setIdUsuario("usuario-777");
        
        gateway.guardar(pedido);

        Optional<Pedido> recuperado = gateway.obtenerPorId(pedido.getId());
        assertTrue(recuperado.isPresent());
        assertEquals("sesion-123", recuperado.get().getSesionId());
        assertEquals(150000, recuperado.get().getTotal());
        assertEquals(EstadoPedido.PENDIENTE, recuperado.get().getEstado());
        assertEquals(TipoMetodoPago.TARJETA, recuperado.get().getTipoMetodoPago());
        assertEquals("usuario-777", recuperado.get().getIdUsuario());
    }

    @Test
    void debeListarPedidosPaginadosPorUsuario() {
        Pedido p1 = new Pedido("sesion-1", 50000, TipoMetodoPago.PAYPAL);
        p1.setIdUsuario("usuario-1");
        gateway.guardar(p1);

        Pedido p2 = new Pedido("sesion-2", 30000, TipoMetodoPago.PAYPAL);
        p2.setIdUsuario("usuario-1");
        gateway.guardar(p2);

        Pedido p3 = new Pedido("sesion-3", 10000, TipoMetodoPago.PAYPAL);
        p3.setIdUsuario("usuario-2");
        gateway.guardar(p3);

        // Listar pedidos de usuario-1, offset 0, limit 10
        List<Pedido> pedidos = gateway.listarPorUsuarioId("usuario-1", 0, 10);
        assertEquals(2, pedidos.size());

        // Verificar paginacion
        List<Pedido> pedidosPaginados = gateway.listarPorUsuarioId("usuario-1", 0, 1);
        assertEquals(1, pedidosPaginados.size());
    }

    @Test
    void debeListarTodosLosPedidosPaginados() {
        gateway.guardar(new Pedido("s-1", 100, TipoMetodoPago.PAYPAL));
        gateway.guardar(new Pedido("s-2", 200, TipoMetodoPago.PAYPAL));
        gateway.guardar(new Pedido("s-3", 300, TipoMetodoPago.PAYPAL));

        List<Pedido> todos = gateway.listarTodos(0, 2);
        assertEquals(2, todos.size());
    }
}
