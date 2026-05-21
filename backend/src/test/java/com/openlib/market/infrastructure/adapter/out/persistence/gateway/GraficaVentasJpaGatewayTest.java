package com.openlib.market.infrastructure.adapter.out.persistence.gateway;

import com.openlib.market.domain.dashboardGlobal.IntervaloTiempo;
import com.openlib.market.domain.dashboardGlobal.SerieGraficaVentas;
import com.openlib.market.domain.pago.EstadoPedido;
import com.openlib.market.domain.pago.Pedido;
import com.openlib.market.domain.pago.TipoMetodoPago;
import com.openlib.market.infrastructure.adapter.out.persistence.PersistenceTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PersistenceTestConfig.class)
@Transactional
@ActiveProfiles("test")
public class GraficaVentasJpaGatewayTest {

    @Autowired
    private GraficaVentasJpaGateway gateway;

    @Autowired
    private PedidoJpaGateway pedidoJpaGateway;

    private static final int ANIO = 2025;

    @BeforeEach
    public void setUp() {
        // Enero 2025: 2 pedidos PAGADOS
        guardarPedidoPagado(200.0, LocalDateTime.of(2025, 1, 10, 10, 0));
        guardarPedidoPagado(150.0, LocalDateTime.of(2025, 1, 25, 15, 0));
        // Febrero 2025: 1 pedido PAGADO
        guardarPedidoPagado(300.0, LocalDateTime.of(2025, 2, 14, 9, 0));
        // Pedido PENDIENTE (no debe contabilizarse)
        Pedido pendiente = new Pedido("sesion-pend", 999.0, TipoMetodoPago.TARJETA);
        pedidoJpaGateway.guardar(pendiente);
        // Pedido de otro año
        guardarPedidoPagado(500.0, LocalDateTime.of(2024, 6, 1, 0, 0));
    }

    private void guardarPedidoPagado(double total, LocalDateTime fecha) {
        Pedido p = new Pedido(
                java.util.UUID.randomUUID().toString(),
                "sesion-g",
                "user-g",
                total,
                EstadoPedido.PAGADO,
                fecha,
                TipoMetodoPago.TARJETA
        );
        pedidoJpaGateway.guardar(p);
    }

    @Test
    public void testAgrupacionMensual() {
        SerieGraficaVentas serie = gateway.generarSerieVentas(ANIO, IntervaloTiempo.MENSUAL);

        assertEquals(IntervaloTiempo.MENSUAL, serie.getIntervalo());
        assertEquals(2, serie.getPuntos().size(), "Debe haber 2 meses: enero y febrero");

        // Enero: 200 + 150 = 350
        double enero = serie.getPuntos().stream()
                .filter(p -> p.getEtiquetaTemporal().equals("2025-01"))
                .mapToDouble(p -> p.getValorAcumulado()).sum();
        assertEquals(350.0, enero, 0.001);

        // Febrero: 300
        double febrero = serie.getPuntos().stream()
                .filter(p -> p.getEtiquetaTemporal().equals("2025-02"))
                .mapToDouble(p -> p.getValorAcumulado()).sum();
        assertEquals(300.0, febrero, 0.001);

        // Total general = 650
        assertEquals(650.0, serie.getTotalAcumuladoSerie(), 0.001);
    }

    @Test
    public void testAgrupacionDiaria() {
        SerieGraficaVentas serie = gateway.generarSerieVentas(ANIO, IntervaloTiempo.DIARIO);

        assertEquals(IntervaloTiempo.DIARIO, serie.getIntervalo());
        // 3 días distintos → 3 puntos
        assertEquals(3, serie.getPuntos().size());
        assertTrue(serie.getPuntos().stream()
                .anyMatch(p -> p.getEtiquetaTemporal().equals("2025-01-10") && p.getValorAcumulado() == 200.0));
    }

    @Test
    public void testAgrupacionSemanal() {
        SerieGraficaVentas serie = gateway.generarSerieVentas(ANIO, IntervaloTiempo.SEMANAL);

        assertEquals(IntervaloTiempo.SEMANAL, serie.getIntervalo());
        // Las 3 transacciones caen en 3 semanas distintas
        assertTrue(serie.getPuntos().size() >= 2, "Deben existir al menos 2 semanas con ventas");
        // Total no varía respecto al mensual
        assertEquals(650.0, serie.getTotalAcumuladoSerie(), 0.001);
    }
}
