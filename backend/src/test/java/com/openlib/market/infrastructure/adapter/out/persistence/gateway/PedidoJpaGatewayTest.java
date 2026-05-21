package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.pago.EstadoPedido;
import com.openlib.market.domain.pago.ItemPedido;
import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.pago.TipoMetodoPago;
import com.openlib.market.infrastructure.adapter.out.persistence.PersistenceTestConfig;
import com.openlib.market.infrastructure.adapter.out.persistence.entity.PedidoEntity;
import com.openlib.market.infrastructure.adapter.out.persistence.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@ActiveProfiles("test")
public class PedidoJpaGatewayTest {

    @Autowired
    private PedidoJpaGateway pedidoJpaGateway;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Test
    public void testGuardadoEnCascadaConItems() {
        Pedido pedido = new Pedido("sesion-123", 50.0, TipoMetodoPago.TARJETA);
        pedido.setIdUsuario("user-1");
        pedido.addItem(new ItemPedido("isbn-1", 1, 20.0));
        pedido.addItem(new ItemPedido("isbn-2", 2, 15.0));

        pedidoJpaGateway.guardar(pedido);

        // Limpiamos el cache de Hibernate si fuera necesario, o leemos directamente
        Optional<Pedido> recuperado = pedidoJpaGateway.obtenerPorId(pedido.getId());

        assertTrue(recuperado.isPresent());
        assertEquals(2, recuperado.get().getItems().size());
        assertEquals("isbn-1", recuperado.get().getItems().get(0).getIsbn());
    }

    @Test
    public void testActualizacionDeEstadoSinDuplicados() {
        Pedido pedido = new Pedido("sesion-456", 100.0, TipoMetodoPago.PAYPAL);
        pedidoJpaGateway.guardar(pedido);

        long countAntes = pedidoRepository.count();

        // Recuperamos, marcamos como pagado
        Pedido recuperado = pedidoJpaGateway.obtenerPorId(pedido.getId()).get();
        recuperado.marcarComoPagado(); // Pasa a PAGADO (COMPLETADO lógicamente, dependiendo del enum)
        
        // Guardamos
        pedidoJpaGateway.guardar(recuperado);

        long countDespues = pedidoRepository.count();

        // No debe haber duplicados
        assertEquals(countAntes, countDespues);

        // Verificamos estado actualizado
        Pedido finalRecuperado = pedidoJpaGateway.obtenerPorId(pedido.getId()).get();
        assertEquals(EstadoPedido.PAGADO, finalRecuperado.getEstado());
    }
}
