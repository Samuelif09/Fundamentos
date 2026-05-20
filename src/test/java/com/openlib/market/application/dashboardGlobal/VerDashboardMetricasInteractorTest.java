package com.openlib.market.application.dashboardGlobal;

import com.openlib.market.domain.dashboardGlobal.IDashboardGlobalGateway;
import com.openlib.market.domain.pago.EstadoPedido;
import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.pago.TipoMetodoPago;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerDashboardMetricasInteractorTest {

    private IDashboardGlobalGateway dashboardGateway;
    private VerDashboardMetricasInteractor interactor;

    @BeforeEach
    void setUp() {
        dashboardGateway = mock(IDashboardGlobalGateway.class);
        interactor = new VerDashboardMetricasInteractor(dashboardGateway);
    }

    @Test
    void debeGenerarGraficaMensualAgrupandoTotal() {
        Pedido p1 = new Pedido("p1", "s1", "u1", 100.0, EstadoPedido.PAGADO, LocalDateTime.of(2026, 1, 15, 10, 0), TipoMetodoPago.TARJETA);
        Pedido p2 = new Pedido("p2", "s2", "u2", 200.0, EstadoPedido.PAGADO, LocalDateTime.of(2026, 1, 20, 10, 0), TipoMetodoPago.PAYPAL);
        Pedido p3 = new Pedido("p3", "s3", "u3", 50.0, EstadoPedido.PAGADO, LocalDateTime.of(2026, 2, 5, 10, 0), TipoMetodoPago.TARJETA);

        when(dashboardGateway.obtenerPedidosExitososDePlataforma(2026)).thenReturn(List.of(p1, p2, p3));

        SerieGraficaDto dto = interactor.generarGraficaVentas("MENSUAL", 2026);

        assertEquals("MENSUAL", dto.getIntervalo());
        assertEquals(350.0, dto.getTotalGlobal(), 0.01);
        assertEquals(12, dto.getPuntos().size()); // 12 meses
        
        // Enero = 300
        assertEquals("ene", dto.getPuntos().get(0).get("etiqueta").toString().toLowerCase().replace(".", "")); // "ene." o "ene"
        assertEquals(300.0, (Double) dto.getPuntos().get(0).get("valor"), 0.01);
        
        // Febrero = 50
        assertEquals("feb", dto.getPuntos().get(1).get("etiqueta").toString().toLowerCase().replace(".", ""));
        assertEquals(50.0, (Double) dto.getPuntos().get(1).get("valor"), 0.01);
    }
}
