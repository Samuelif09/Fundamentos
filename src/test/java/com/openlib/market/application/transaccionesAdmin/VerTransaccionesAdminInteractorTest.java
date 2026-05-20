package com.openlib.market.application.transaccionesAdmin;

import com.openlib.market.domain.pago.EstadoPedido;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.pago.TipoMetodoPago;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerTransaccionesAdminInteractorTest {

    private IPedidoGateway pedidoGateway;
    private VerTransaccionesAdminInteractor interactor;

    @BeforeEach
    void setUp() {
        pedidoGateway = mock(IPedidoGateway.class);
        interactor = new VerTransaccionesAdminInteractor(pedidoGateway);
    }

    @Test
    void debeListarTodasLasTransaccionesPaginadas() {
        Pedido p1 = new Pedido("ped1", "ses1", "user1", 50.0, EstadoPedido.PAGADO, LocalDateTime.now(), TipoMetodoPago.TARJETA);
        Pedido p2 = new Pedido("ped2", "ses2", "user2", 120.0, EstadoPedido.PENDIENTE, LocalDateTime.now(), TipoMetodoPago.PAYPAL);
        when(pedidoGateway.listarTodos(0, 50)).thenReturn(List.of(p1, p2));

        List<TransaccionGlobalDto> result = interactor.listarTransacciones(0, 50);

        assertEquals(2, result.size());
        assertEquals("ped1", result.get(0).getIdPedido());
        assertEquals("PAGADO", result.get(0).getEstado());
        assertEquals("ped2", result.get(1).getIdPedido());
        assertEquals(120.0, result.get(1).getMontoTotal());
    }

    @Test
    void debeRetornarListaVaciaSiNoHayTransacciones() {
        when(pedidoGateway.listarTodos(0, 50)).thenReturn(List.of());
        List<TransaccionGlobalDto> result = interactor.listarTransacciones(0, 50);
        assertTrue(result.isEmpty());
    }
}
