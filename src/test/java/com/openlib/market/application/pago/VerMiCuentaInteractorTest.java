package com.openlib.market.application.pago;

import com.openlib.market.domain.pago.EstadoPedido;
import com.openlib.market.domain.pago.IPedidoGateway;
import com.openlib.market.domain.pago.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerMiCuentaInteractorTest {

    private IPedidoGateway pedidoGateway;
    private VerMiCuentaInteractor interactor;

    @BeforeEach
    void setUp() {
        pedidoGateway = mock(IPedidoGateway.class);
        interactor = new VerMiCuentaInteractor(pedidoGateway);
    }

    @Test
    void debeRetornarListaVaciaSiNoHayPedidos() {
        when(pedidoGateway.listarPorUsuarioId("user-1", 0, 10)).thenReturn(Collections.emptyList());

        List<HistorialPedidoResponseDto> result = interactor.obtenerHistorial("user-1", 0, 10);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void debeRetornarHistorialMapeadoCorrectamente() {
        Pedido p1 = new Pedido("id-1", "sesion-1", "user-1", 100.0, EstadoPedido.PAGADO, LocalDateTime.now(), com.openlib.market.domain.pago.TipoMetodoPago.TARJETA);
        when(pedidoGateway.listarPorUsuarioId("user-1", 0, 10)).thenReturn(List.of(p1));

        List<HistorialPedidoResponseDto> result = interactor.obtenerHistorial("user-1", 0, 10);

        assertEquals(1, result.size());
        assertEquals("id-1", result.get(0).getId());
        assertEquals(100.0, result.get(0).getTotal());
        assertEquals("PAGADO", result.get(0).getEstado());
    }
}
